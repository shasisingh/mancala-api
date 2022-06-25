package com.shashi.bol.mancala.game.v1.service;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.repository.MancalaGameRepository;
import com.shashi.bol.mancala.game.v1.repository.PitRepository;
import com.shashi.bol.mancala.game.v1.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.shashi.bol.mancala.game.v1.utils.GameUtils.displayBoard;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.game5Move;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getGameId;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getNewGame;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getNewGame1;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getNewGame3;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getNewGame4;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getNewGame5;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getNewGame6;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getNewGamePlayer1Win;
import static com.shashi.bol.mancala.game.v1.utils.GameUtils.getTieGame;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class MancalaBoardServiceTest {

    @Mock
    private MancalaGameRepository mancalaGameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PitRepository pitsRepository;

    @InjectMocks
    MancalaBoardService service;

    @Test
     void testFirstMove() {
        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame(gameId);
        mancalaGame.setPitsMoved(false);
        mancalaGame.setMancalaPits(game5Move());
        mancalaGame.setCurrentPitIndex(0);
        mancalaGame.getPlayer1().setTurn(false);
        mancalaGame.getPlayer2().setTurn(false);

        when(mancalaGameRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(mancalaGameRepository.findByGameId(gameId)).thenReturn(Optional.of(mancalaGame));
        displayBoard(mancalaGame, "OLD:VALUE");

        MancalaGame game = service.makeMove(gameId, 1);   // pits have 6 stones


        assertThat("gameId", game.getGameId(), is(gameId));
        assertThat("moved", game.isPitsMoved(), is(true));
        assertThat("status", game.getGameStatus(), is(MancalaGame.GameStatus.ON_GOING));
        assertThat("player1 turn ", game.getPlayer1().isTurn(), is(true));
        assertThat("player2 turn ", game.getPlayer2().isTurn(), is(false));
        assertThat("last pit index", game.getCurrentPitIndex(), is(7));

        //verify with expected data.
        validatePits(List.of(0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0), game);

        displayBoard(game, "UPDATED:VALUE-1");

        game = service.makeMove(gameId, 2);   // pits have 7 stones

       //verify with expected data.

       validatePits(List.of(0, 0, 8, 8, 8, 8, 2, 7, 7, 6, 6, 6, 6, 0), game);

       displayBoard(game, "UPDATED:VALUE-2");

       assertThat("last pit index", game.getCurrentPitIndex(), is(9));

       verify(playerRepository,times(2)).saveAll(anyList());
        verify(pitsRepository,times(2)).saveAll(anyList());

    }

    @ParameterizedTest
    @DisplayName("Validate:player turn based on there selected pit location")
    @ValueSource(ints = {6,10})
    void testFirstMoveCheckingMoves(int pitLocation) {
        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame(gameId);
        mancalaGame.setPitsMoved(false);
        mancalaGame.setMancalaPits(game5Move());
        mancalaGame.setCurrentPitIndex(0);
        mancalaGame.getPlayer1().setTurn(false);
        mancalaGame.getPlayer2().setTurn(false);

        when(mancalaGameRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(mancalaGameRepository.findByGameId(gameId)).thenReturn(Optional.of(mancalaGame));
        displayBoard(mancalaGame, "OLD:VALUE");

        MancalaGame game = service.makeMove(gameId, pitLocation);   // pits have 6 stones

        assertThat("moved", game.isPitsMoved(), is(true));
        assertThat("status", game.getGameStatus(), is(MancalaGame.GameStatus.ON_GOING));

        if (pitLocation == 6) {
            assertThat("player1 turn ", game.getPlayer1().isTurn(), is(false));// with move 6 player loose chance
            assertThat("player1 turn ", game.getPlayer1().isPrevTurn(), is(true));// he was moved last time.
            assertThat("player2 turn ", game.getPlayer2().isTurn(), is(true));//player 2 got chance
        }
        if (pitLocation == 10) {
            assertThat("player2 turn ", game.getPlayer2().isTurn(), is(false));// with move 10 player loose chance
            assertThat("player2 turn ", game.getPlayer2().isPrevTurn(), is(true));//// he was moved last time.
            assertThat("player1 turn ", game.getPlayer1().isTurn(), is(true));//player 1 got chance.
        }



    }

    @Test
    @DisplayName("Game: checking with move 12:if player 2 get turn as it's in Home pit/mancala pit")
     void verifyingPlayerGetTurnIfHomePit() {
        String gameId = getGameId();
        MancalaGame newGame = getNewGame(gameId);
        when(mancalaGameRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(mancalaGameRepository.findByGameId(gameId)).thenReturn(Optional.of(newGame));

        displayBoard(newGame, "OLD:VALUE");

        MancalaGame game = service.makeMove(gameId, 12);   // pits have 3 stones
        assertAll("move with game Id:"+gameId,

                () -> assertThat("gameId not matching", game.getGameId(), is(gameId)),
                () -> assertThat("game status", game.getGameStatus(), is(MancalaGame.GameStatus.ON_GOING)),
                () -> assertThat("player1 turn ", game.getPlayer1().isTurn(), is(false)),
                () -> assertThat("player2 turn ", game.getPlayer2().isTurn(), is(true)),
                () -> assertThat("last pit index", game.getCurrentPitIndex(), is(14)));

        //verify with expected data.
        validatePits(List.of(0, 3, 11, 9, 0, 1, 12, 2, 2, 10, 3, 0, 14, 5), game);

        displayBoard(game, "UPDATED:VALUE");
    }

    @Test
    @DisplayName("WINNER MOVE:: checking with move 6:if player 1 lost the game:player2 is winner")
     void withLastMovePlayer2_isWinner() {
        String gameId = getGameId();
        when(mancalaGameRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(getNewGame1(gameId)));

        MancalaGame game = service.makeMove(gameId, 6);
        assertAll("move with game Id "+gameId,

                () -> assertThat("gameId", game.getGameId(), is(gameId)),
                () -> assertThat("gameId", game.getGameStatus(), is(MancalaGame.GameStatus.FINISHED)),
                () -> assertThat("player1 turn ", game.getPlayer1().isTurn(), is(true)),
                () -> assertThat("player2 turn ", game.getPlayer2().isTurn(), is(false)),
                () -> assertThat("player2 winner ", game.getPlayer2().getStatus(), is("WINNER")),
                () -> assertThat("last pit index", game.getCurrentPitIndex(), is(7)));

        //verify with expected data.
        validatePits(List.of(0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 36), game);

        displayBoard(game, "UPDATED:VALUE");
    }

    @Test
    @DisplayName("WINNER MOVE: checking with move 5:if player 2 lost the game:player1 is winner")
     void withLastMovePlayer1_isWinner() {
        String gameId = getGameId();
        when(mancalaGameRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(getNewGamePlayer1Win(gameId)));

        MancalaGame game = service.makeMove(gameId, 5);
        assertAll("move with game Id "+gameId,

                () -> assertThat("gameId", game.getGameId(), is(gameId)),
                () -> assertThat("gameId", game.getGameStatus(), is(MancalaGame.GameStatus.FINISHED)),
                () -> assertThat("player1 turn ", game.getPlayer1().isTurn(), is(false)),
                () -> assertThat("player2 turn ", game.getPlayer2().isTurn(), is(true)),
                () -> assertThat("player1 winner ", game.getPlayer1().getStatus(), is("WINNER")),
                () -> assertThat("last pit index", game.getCurrentPitIndex(), is(6)));

        //verify with expected data.
        validatePits(List.of(0, 0, 0, 0, 0, 0, 37, 0, 0, 0, 0, 0, 0, 17), game);

        displayBoard(game, "UPDATED:VALUE");
    }

    @Test
    @DisplayName("TIE MOVE: checking with move 5:if Game is tie")
     void withLastMoveGameIsTie() {
        String gameId = getGameId();
        when(mancalaGameRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(getTieGame(gameId)));

        MancalaGame game = service.makeMove(gameId, 5);
        assertAll("move with game Id "+gameId,

                () -> assertThat("gameId", game.getGameId(), is(gameId)),
                () -> assertThat("gameId", game.getGameStatus(), is(MancalaGame.GameStatus.TIE)),
                () -> assertThat("player1 turn ", game.getPlayer1().isTurn(), is(false)),
                () -> assertThat("player2 turn ", game.getPlayer2().isTurn(), is(true)),
                () -> assertThat("player1 winner ", game.getPlayer1().getStatus(), nullValue()),
                () -> assertThat("player2 winner ", game.getPlayer2().getStatus(), nullValue()),
                () -> assertThat("last pit index", game.getCurrentPitIndex(), is(6)));

        //verify with expected data.
        validatePits(List.of(0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 32), game);

        displayBoard(game, "UPDATED:VALUE");
    }

    @Test
    @DisplayName("Error: requested game id not found")
     void errorWhileCallingGameIdFromDb() {
        String gameId = getGameId();
        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.makeMove(gameId, 1))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("reason","Game gameId "+gameId+" not found.")
                .hasFieldOrPropertyWithValue("status",HttpStatus.NOT_FOUND);

    }


    @Test
     void checkSomeSpecialCaseItWasFailedHereWithGameSet_getNewGame5() {

        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame5(gameId);
        when(mancalaGameRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(mancalaGameRepository.findByGameId(gameId)).thenReturn(Optional.of(mancalaGame));

        displayBoard(mancalaGame, "OLD:VALUE");

        mancalaGame = service.makeMove(gameId, 12);

        assertThat("gameId:", mancalaGame.getGameId(), is(gameId));
        assertThat("game status", mancalaGame.getGameStatus(), is(MancalaGame.GameStatus.ON_GOING));

        assertThat("player1 turn ", mancalaGame.getPlayer1().isTurn(), is(true));
        assertThat("player2 turn ", mancalaGame.getPlayer2().isTurn(), is(false));
        assertThat("last pit index", mancalaGame.getCurrentPitIndex(), is(4));

        //verify with expected data.
        validatePits(List.of(7, 7, 7, 7, 6, 6, 0, 6, 6, 6, 6, 0, 7, 1), mancalaGame);

        displayBoard(mancalaGame, "UPDATED:VALUE");
    }


    @ParameterizedTest
    @MethodSource("providePitsAndPlayerTurn")
    @DisplayName("checking all invalid moves by player1")
     void makeMoveIsInvalid_selectedPits_Is_Player1Home(int args,boolean player1Turn) {
        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame6(gameId);
        mancalaGame.getPlayer1().setTurn(player1Turn);

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(mancalaGame));

        displayBoard(mancalaGame, "OLD:VALUE");
        mancalaGame = service.makeMove(gameId, args);

        //verify with expected data.
        validatePits(List.of(1, 0, 1, 1, 1, 13, 23, 0, 0, 12, 0, 0, 0, 20), mancalaGame);

        assertThat("player2 turn ", mancalaGame.getPlayer1().isTurn(), is(true));

        displayBoard(mancalaGame, "UPDATED:VALUE-NOT HAVE ANY CHANGE");

        verify(mancalaGameRepository,times(0)).save(any());
    }

   @ParameterizedTest
   @MethodSource("providePitsAndPlayer2Turn")
   @DisplayName("checking all invalid moves by player 2")
   void makeMoveIsInvalid_selectedPits_Is_Player2Home(int args,boolean player2Turn) {
      String gameId = getGameId();
      MancalaGame mancalaGame = getNewGame6(gameId);
      mancalaGame.getPlayer2().setTurn(player2Turn);

      when(mancalaGameRepository.findByGameId(gameId))
              .thenReturn(Optional.of(mancalaGame));

      displayBoard(mancalaGame, "OLD:VALUE");
      mancalaGame = service.makeMove(gameId, args);

      //verify with expected data.
      validatePits(List.of(1, 0, 1, 1, 1, 13, 23, 0, 0, 12, 0, 0, 0, 20), mancalaGame);

       assertThat("player2 turn ", mancalaGame.getPlayer2().isTurn(), is(true));

      displayBoard(mancalaGame, "UPDATED:VALUE-NOT HAVE ANY CHANGE");

      verify(mancalaGameRepository,times(0)).save(any());
   }



    @Test
     void makeMoveIsInvalid_PlayingWithAlreadyEndGame_finished_game() {
        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame6(gameId);
        mancalaGame.setGameStatus(MancalaGame.GameStatus.FINISHED);
        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(mancalaGame));

        displayBoard(mancalaGame, "OLD:VALUE");

        mancalaGame = service.makeMove(gameId, 1);

        //verify with expected data.
        validatePits(List.of(1, 0, 1, 1, 1, 13, 23, 0, 0, 12, 0, 0, 0, 20), mancalaGame);

        displayBoard(mancalaGame, "UPDATED:VALUE-NOT HAVE ANY CHANGE");

        verify(mancalaGameRepository, times(0))
                .save(any());
    }

    @Test
     void makeMoveIsInvalid_PlayingWithAlreadyEndGame_tie_game() {
        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame6(gameId);
        mancalaGame.setGameStatus(MancalaGame.GameStatus.TIE);
        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(mancalaGame));

        displayBoard(mancalaGame, "OLD:VALUE");

        mancalaGame = service.makeMove(gameId, 1);

        //verify with expected data.
        validatePits(List.of(1, 0, 1, 1, 1, 13, 23, 0, 0, 12, 0, 0, 0, 20), mancalaGame);

        displayBoard(mancalaGame, "UPDATED:VALUE-NOT HAVE ANY CHANGE");

        verify(mancalaGameRepository, times(0))
                .save(any());
    }

    @Test
     void makeMoveIsInvalid_selectedPits_Is_player1pit_butNot_Player1_turn() {
        String gameId = getGameId();

        MancalaGame mancalaGame = getNewGame6(gameId);
        mancalaGame.getPlayer1().setTurn(false);
        mancalaGame.getPlayer2().setTurn(true);

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(mancalaGame));

        displayBoard(mancalaGame, "OLD:VALUE");

        mancalaGame = service.makeMove(gameId, 1);// requested pits belong to player1

        //verify with expected data.
        validatePits(List.of(1, 0, 1, 1, 1, 13, 23, 0, 0, 12, 0, 0, 0, 20), mancalaGame);

        displayBoard(mancalaGame, "UPDATED:VALUE-NOT HAVE ANY CHANGE");
        verify(mancalaGameRepository, times(0)).save(any());
    }

    @Test
     void makeMoveIsInvalid_selectedPitIsEmpty() {
        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame6(gameId);
        mancalaGame.getPlayer1().setTurn(true);
        mancalaGame.getPlayer2().setTurn(false);
        mancalaGame.getMancalaPits().get(0).setStones(0);

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(mancalaGame));

        displayBoard(mancalaGame, "OLD:VALUE");
        mancalaGame = service.makeMove(gameId, 1);// requested pits belong to player1

        //verify with expected data.
        validatePits(List.of(0, 0, 1, 1, 1, 13, 23, 0, 0, 12, 0, 0, 0, 20), mancalaGame);

        displayBoard(mancalaGame, "UPDATED:VALUE-NOT HAVE ANY CHANGE");

        verify(mancalaGameRepository, times(0)).save(any());
    }

    @Test
     void makeMoveIsInvalid_selectedPits_Is_player2Pit_but_Player1_turn() {
        String gameId = getGameId();
        MancalaGame mancalaGame = getNewGame6(gameId);

        mancalaGame.getPlayer1().setTurn(true);
        mancalaGame.getPlayer2().setTurn(false);

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(mancalaGame));

        displayBoard(mancalaGame, "OLD:VALUE");

        mancalaGame = service.makeMove(gameId, 12);// requested pits belong to player2

        //verify with expected data.
        validatePits(List.of(1, 0, 1, 1, 1, 13, 23, 0, 0, 12, 0, 0, 0, 20), mancalaGame);

        displayBoard(mancalaGame, "UPDATED:VALUE-NOT HAVE ANY CHANGE");

        verify(mancalaGameRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Game3:with move from pit:1: player 1 complete both(one full round) Home pits and should not get another chance")
     void testing_player1WithFullRoundHome_To_Home_andNot_get_another_chance() {

        String gameId = getGameId();
        when(mancalaGameRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(mancalaGameRepository.findByGameId(gameId))
                .thenReturn(Optional.of(getNewGame3(gameId)));

        MancalaGame mancalaGame = service.makeMove(gameId, 1);

       assertThat("gameId", mancalaGame.getGameId(), is(gameId));
       assertThat("gameId", mancalaGame.getGameStatus(), is(MancalaGame.GameStatus.ON_GOING));
       assertThat("player1 turn ", mancalaGame.getPlayer1().isTurn(), is(false));
       assertThat("player2 turn ", mancalaGame.getPlayer2().isTurn(), is(true));
       assertThat("last pit index", mancalaGame.getCurrentPitIndex(), is(14));

        //verify with expected data.
        validatePits(List.of(0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), mancalaGame);
    }



    void validatePits(List<Integer> expectedPits, final MancalaGame game) {
        AtomicInteger index = new AtomicInteger(1);
        expectedPits.forEach(expected -> {
            assertThat("value@pit-location:" + index.get() + ", not as expected", game.getPit(index.get()).getStones(), is(expected));
            index.set(index.get() + 1);
        });
    }


   private static Stream<Arguments> providePitsAndPlayerTurn() {
      return Stream.of(
              Arguments.of(10, true),
              Arguments.of(14, true),
              Arguments.of(7, true),
              Arguments.of(9, true)
      );
   }

   private static Stream<Arguments> providePitsAndPlayer2Turn() {
      return Stream.of(
              Arguments.of(5, true),
              Arguments.of(1, true),
              Arguments.of(6, true),
              Arguments.of(7, true)
      );
   }


}
