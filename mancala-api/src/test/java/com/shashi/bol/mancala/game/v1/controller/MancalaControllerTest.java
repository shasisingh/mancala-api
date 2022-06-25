package com.shashi.bol.mancala.game.v1.controller;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.domain.MancalaPit;
import com.shashi.bol.mancala.game.v1.domain.Player;
import com.shashi.bol.mancala.game.v1.mapper.DTOMapper;
import com.shashi.bol.mancala.game.v1.model.GameDTO;
import com.shashi.bol.mancala.game.v1.service.MancalaBoardService;
import com.shashi.bol.mancala.game.v1.service.MancalaGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.DEFAULT_PLAYER1_NAME;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.DEFAULT_PLAYER2_NAME;
import static com.shashi.bol.mancala.game.v1.utils.ReflectionUtil.set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MancalaControllerTest {

    @InjectMocks
     MancalaController controller;

    @Mock
     private MancalaGameService bookService;

    @Mock
    private DTOMapper mapper;

    @Mock
     private MancalaBoardService mancalaBoardService;


    @BeforeEach
      void setup() throws NoSuchFieldException, IllegalAccessException {
       set(controller,"defaultPitStones",6);
        lenient()
                .when(mapper.mapToDto(any()))
                .thenCallRealMethod();
    }


    @Test
     void createGameWithDefaultPits() {
        when(bookService.createGame(anyInt()))
                .thenReturn(getGame("gameId"));

        ResponseEntity<GameDTO> game = controller.createGame(null, "", "");
        assertNotNull(game);
        assertThat("status",game.getStatusCode(),is(HttpStatus.OK));
        assertThat("gameId",game.getBody().getGameId(),notNullValue());
        assertThat("GAME STATUS",game.getBody().getGameStatus(),is(MancalaGame.GameStatus.INITIATED));
        assertThat("GAME STONE VALUE",game.getBody().getMancalaPits().get(0).getStones(),is(1));

        verify(bookService).createGame(6);//default stones

    }

    @Test
     void createGameWithRequiredPits() {

        when(bookService.createGame(anyInt()))
                .thenReturn(getGame("gameId"));

        ResponseEntity<GameDTO> game = controller.createGame(4, "", "");
        assertNotNull(game);
        assertThat("status",game.getStatusCode(),is(HttpStatus.OK));
        assertThat("gameId",game.getBody().getGameId(),notNullValue());
        assertThat("GAME STATUS",game.getBody().getGameStatus(),is(MancalaGame.GameStatus.INITIATED));

        verify(bookService).createGame(4);//supplied stones

    }

    @Test
     void createGameWithRequiredPitsWithPlayerName() {

                when(bookService.createGame(anyInt(),any(),any()))
                        .thenReturn(getGame("gameId"));


        ResponseEntity<GameDTO> game = controller.createGame(4, "1", "2");
        assertNotNull(game);
        assertThat("status",game.getStatusCode(),is(HttpStatus.OK));
        assertThat("gameId",game.getBody().getGameId(),notNullValue());
        assertThat("GAME STATUS",game.getBody().getGameStatus(),is(MancalaGame.GameStatus.INITIATED));

        verify(bookService).createGame(4,"1","2");//supplied stones

    }


    @Test
     void createGameWithRequiredPitsWithPlayerName1Only() {
       assertThatThrownBy(() -> controller.createGame(4, "1", ""))
               .isInstanceOf(ResponseStatusException.class)
               .hasFieldOrPropertyWithValue("reason","Wrong entry, must provide both player name")
               .hasFieldOrPropertyWithValue("status",HttpStatus.BAD_REQUEST);

        verifyNoMoreInteractions(bookService);

    }

    @Test
     void playGame_makeAMove() {
                when(mancalaBoardService.makeMove(any(),anyInt()))
                        .thenReturn(getGame("gameId"));

        ResponseEntity<GameDTO> newMove = controller.play("gameId", 5);
        assertNotNull(newMove);
        assertThat("status",newMove.getStatusCode(),is(HttpStatus.OK));
        assertThat("gameId",newMove.getBody().getGameId(),notNullValue());
        assertThat("GAME STATUS",newMove.getBody().getGameStatus(),is(MancalaGame.GameStatus.INITIATED));
        verify(mancalaBoardService).makeMove("gameId",5);//supplied stones
    }


    @Test
     void exceptionCheckInvalidPits() {
                when(mancalaBoardService.makeMove(any(),anyInt()))
                        .thenReturn(getGame("gameId"));

        assertThatThrownBy(() -> controller.play("gameId", null))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("reason","Invalid pit Index!. It should be between 1..6 or 8..13")
                .hasFieldOrPropertyWithValue("status",HttpStatus.BAD_REQUEST);

        assertThatThrownBy(() -> controller.play("gameId", 0))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("reason","Invalid pit Index!. It should be between 1..6 or 8..13")
                .hasFieldOrPropertyWithValue("status",HttpStatus.BAD_REQUEST);

        assertThatThrownBy(() -> controller.play("gameId", 15))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("reason","Invalid pit Index!. It should be between 1..6 or 8..13")
                .hasFieldOrPropertyWithValue("status",HttpStatus.BAD_REQUEST);

        assertThatThrownBy(() -> controller.play("gameId", -1))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("reason","Invalid pit Index!. It should be between 1..6 or 8..13")
                .hasFieldOrPropertyWithValue("status",HttpStatus.BAD_REQUEST);


        assertThatThrownBy( () -> controller.play("gameId", 7));
        assertThatThrownBy( () -> controller.play("gameId", 14));

        assertDoesNotThrow( () ->  controller.play("gameId", 1));
        assertDoesNotThrow( () ->  controller.play("gameId", 13));


    }

    @Test
     void testGameStatus() {
                when(mancalaBoardService.loadGame(any()))
                        .thenReturn(getGame("gameId"));

        ResponseEntity<GameDTO> gameStatus = controller.gameStatus("gameId");
        assertNotNull(gameStatus);
        assertThat("status",gameStatus.getStatusCode(),is(HttpStatus.OK));
        assertThat("gameId",gameStatus.getBody().getGameId(),notNullValue());
        assertThat("GAME STATUS",gameStatus.getBody().getGameStatus(),is(MancalaGame.GameStatus.INITIATED));
        verify(mancalaBoardService).loadGame("gameId");
    }



    private static MancalaGame getGame(String gameId){
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setPitsMoved(false);
        game.setMancalaPits(defaultMove());
        game.setPlayer1(getPlayer(DEFAULT_PLAYER1_NAME));
        game.setPlayer2(getPlayer(DEFAULT_PLAYER2_NAME));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        return game;
    }

    private static Player getPlayer(String player1){
        Player player= new Player();
        player.setName(player1);
        return player;
    }

    private static List<MancalaPit> defaultMove() {
        return List.of(
                new MancalaPit(1,1),
                new MancalaPit(2,0),
                new MancalaPit(3,1),
                new MancalaPit(4,1),
                new MancalaPit(5,1),
                new MancalaPit(6,13),

                new MancalaPit(7,23),//HOME player1

                new MancalaPit(8,0),
                new MancalaPit(9,0),
                new MancalaPit(10,12),
                new MancalaPit(11,0),
                new MancalaPit(12,0),
                new MancalaPit(13,0),

                new MancalaPit(14,20) //HOME player2
        );

    }

}
