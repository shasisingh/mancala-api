package com.shashi.bol.mancala.game.v1.service;

import com.shashi.bol.mancala.game.v1.api.MancalaBoardApi;
import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.domain.MancalaPit;
import com.shashi.bol.mancala.game.v1.domain.Player;
import com.shashi.bol.mancala.game.v1.repository.MancalaGameRepository;
import com.shashi.bol.mancala.game.v1.repository.PitRepository;
import com.shashi.bol.mancala.game.v1.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.shashi.bol.mancala.game.v1.domain.MancalaGame.GameStatus.ON_GOING;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.EMPTY_STONE;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.FIRST_PIT_PLAYER_B;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.LEFT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.RIGHT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.SIXTH_PIT_PLAYER_A;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.SIXTH_PIT_PLAYER_B;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.TOTAL_PITS;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.WINNER_TAG;
import static com.shashi.bol.mancala.game.v1.domain.MancalaGame.GameStatus.FINISHED;
import static com.shashi.bol.mancala.game.v1.domain.MancalaGame.GameStatus.TIE;
import static java.lang.Boolean.TRUE;

@Service
public class MancalaBoardService implements MancalaBoardApi  {

    private final static Logger LOG = LoggerFactory.getLogger(MancalaBoardService.class);

    private final MancalaGameRepository mancalaGameRepository;
    private final PlayerRepository playerRepository;
    private final PitRepository pitsRepository;
    private MancalaGame gameBoard;

    public MancalaBoardService(MancalaGameRepository mancalaGameRepository, PlayerRepository playerRepository, PitRepository pitsRepository) {
        this.mancalaGameRepository = mancalaGameRepository;
        this.playerRepository = playerRepository;
        this.pitsRepository = pitsRepository;
    }

    @Override
    public MancalaGame makeMove(String gameId, int requestedPitId) {
        this.gameBoard = loadGame(gameId);

        if (!isValidMove(requestedPitId)) {
            return gameBoard;
        }

        LOG.debug("Stones in selected pit:{}",gameBoard.getPit(requestedPitId));

        if(!gameBoard.isPitsMoved()) {
            updatePlayerTurn(requestedPitId);
        }
        allocateStones(requestedPitId);
        captureIfLastPitIsOwnEmptyPit();
        switchTurnsIfLastPitIsNotOwnMancala();
        collectLastStonesIfGameIsOver();
        return saveGameBoard();
    }

    @Override
    public boolean isEmpty(final int index) {
        return getStonesFromPit(index) == 0;
    }

    @Override
    public boolean isGameOver() {
        return getTotalStonesFromPlayer1() == 0 || getTotalStonesFromPlayer2() == 0 ;
    }

    @Override
    public boolean isValidMove(int selectedPit) {
        return !(
                   (selectedPit >= LEFT_PIT_HOUSE_ID || selectedPit == RIGHT_PIT_HOUSE_ID || isEmpty(selectedPit))
                || ( FINISHED.equals(getCurrentStatus()) || TIE.equals(getCurrentStatus()))
                || (gameBoard.getPlayer1().isTurn() && selectedPit > RIGHT_PIT_HOUSE_ID)
                || (gameBoard.getPlayer2().isTurn() && selectedPit < RIGHT_PIT_HOUSE_ID)
                );
    }

    @Override
    public MancalaGame.GameStatus getCurrentStatus() {
        return gameBoard.getGameStatus();
    }


    @Override
    public int getTotalStonesFromPlayer1() {
        return gameBoard
                .getMancalaPits()
                .subList(0,SIXTH_PIT_PLAYER_A)
                .stream()
                .map(MancalaPit::getStones)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public int getTotalStonesFromPlayer2() {
        return gameBoard
                .getMancalaPits()
                .subList(FIRST_PIT_PLAYER_B - 1, SIXTH_PIT_PLAYER_B)
                .stream()
                .map(MancalaPit::getStones)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public boolean isHomePit() {
        return gameBoard.getCurrentPitIndex() == RIGHT_PIT_HOUSE_ID || gameBoard.getCurrentPitIndex() == LEFT_PIT_HOUSE_ID;
    }

    public MancalaGame loadGame(String gameId) {
        return mancalaGameRepository.findByGameId(gameId)
                .orElseThrow(() -> {
                    LOG.error("Game id {} not found.",gameId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Game gameId " + gameId + " not found.");
                });
    }

    private void collectLastStonesIfGameIsOver() {
        if (isGameOver()) {
            collectStones();
            emptyRegularPits();
            endGame();
        }
    }

    private void endGame() {
        int player1HomeCount = gameBoard.getPit(RIGHT_PIT_HOUSE_ID).getStones();
        int player2HomeCount = gameBoard.getPit(LEFT_PIT_HOUSE_ID).getStones();

        if (player1HomeCount != player2HomeCount) {
            if (player1HomeCount > player2HomeCount) {
                gameBoard.getPlayer1().setStatus(WINNER_TAG);
            } else {
                gameBoard.getPlayer2().setStatus(WINNER_TAG);
            }
            gameBoard.setGameStatus(FINISHED);
        } else {
            gameBoard.setGameStatus(MancalaGame.GameStatus.TIE);
        }
    }

    private void collectStones() {
        final int totalStonesFromPlayer1 = getTotalStonesFromPlayer1();
        final int totalStonesFromPlayer2 = getTotalStonesFromPlayer2();
        int player1Stones = getStonesFromPit(RIGHT_PIT_HOUSE_ID) + totalStonesFromPlayer1;
        int player2Stones = getStonesFromPit(LEFT_PIT_HOUSE_ID) + totalStonesFromPlayer2;

        setStonesInPit(RIGHT_PIT_HOUSE_ID, player1Stones);
        setStonesInPit(LEFT_PIT_HOUSE_ID, player2Stones);
    }

    private void emptyRegularPits() {
        gameBoard.getMancalaPits()
                .stream()
                .filter(pit -> !(
                            pit.getPitLocation() == LEFT_PIT_HOUSE_ID
                        ||  pit.getPitLocation() == RIGHT_PIT_HOUSE_ID)
                )
                .forEach(pit -> pit.setStones(EMPTY_STONE));

    }

    private void switchTurnsIfLastPitIsNotOwnMancala() {
        if (isNotOwnMancala(gameBoard.getCurrentPitIndex())) {
            nextTurn();
        }
    }

    private boolean isNotOwnMancala(final int pitLocation) {
        return gameBoard.getPlayer1().isTurn() && pitLocation != RIGHT_PIT_HOUSE_ID
                || gameBoard.getPlayer2().isTurn() && pitLocation != LEFT_PIT_HOUSE_ID;
    }



    private int getStonesFromPit(final int pitLocation) {
        return gameBoard.getPit(pitLocation).getStones();
    }

    private void setStonesInPit(final int pitLocation, final int numberOfStones) {
        gameBoard.getPit(pitLocation).setStones(numberOfStones);
    }

    private void emptyPit(final int pitLocation) {
       gameBoard.getPit(pitLocation).setStones(EMPTY_STONE);
    }

    private void updatePlayerTurn(int requestedPitId) {
        if (requestedPitId < RIGHT_PIT_HOUSE_ID) {
            updatePlayer(gameBoard.getPlayer1(), true, true);
        } else {
            updatePlayer(gameBoard.getPlayer2(), true, true);
        }
        gameBoard.setPitsMoved(true);
        gameBoard.setGameStatus(ON_GOING);
    }


    private void allocateStones(int requestedPit) {
        gameBoard.setCurrentPitIndex(requestedPit);
        MancalaPit selectedPit = gameBoard.getPit(requestedPit);
        int stones = selectedPit.getStones();
        emptyPit(requestedPit);
        for (int value = 0; value < stones; value++) {
            int currentPitIndex = gameBoard.getCurrentPitIndex() % TOTAL_PITS + 1;
            gameBoard.setCurrentPitIndex(currentPitIndex);
            MancalaPit targetPit = gameBoard.getPit(currentPitIndex);
            targetPit.update();
        }
    }


    // sow the game one pit to the right
    private void captureIfLastPitIsOwnEmptyPit() {
        if (!isHomePit()) {
            int currentPitIndex = gameBoard.getCurrentPitIndex();
            MancalaPit targetPit = gameBoard.getPit(currentPitIndex);
            MancalaPit oppositePit = gameBoard.getPit(TOTAL_PITS - currentPitIndex);

            boolean ownPit = isPlayerOwnPit(gameBoard, oppositePit.getPitLocation());
            if (!ownPit && targetPit.getStones() == 1 && TRUE.equals(!oppositePit.isEmpty())) {
                int oppositeStones = oppositePit.getStones();
                oppositePit.clear();
                Integer pitHouseIndex = currentPitIndex < RIGHT_PIT_HOUSE_ID ? RIGHT_PIT_HOUSE_ID : LEFT_PIT_HOUSE_ID;
                MancalaPit pitHouse = gameBoard.getPit(pitHouseIndex);
                pitHouse.addStones(oppositeStones + 1);
                targetPit.setStones(EMPTY_STONE);
            }
        }
    }

    private void nextTurn() {
        if (gameBoard.getPlayer1().isTurn()) {
            updatePlayer(gameBoard.getPlayer2(), true,false);
            updatePlayer(gameBoard.getPlayer1(), false,true);
        } else {
            updatePlayer(gameBoard.getPlayer1(), true,false);
            updatePlayer(gameBoard.getPlayer2(), false,true);
        }

    }

    private boolean isPlayerOwnPit(MancalaGame game, int pitIndex) {
        if (game.getPlayer1().isTurn()) {
            return pitIndex < RIGHT_PIT_HOUSE_ID;
        }
        return pitIndex > RIGHT_PIT_HOUSE_ID;

    }

    private void updatePlayer(Player player, Boolean isTurn, Boolean isPrevTurn) {
        if (isTurn != null) {
            player.setTurn(isTurn);
        }
        if (isPrevTurn != null) {
            player.setPrevTurn(isPrevTurn);
        }

    }

    private MancalaGame saveGameBoard(){
        playerRepository.saveAll(List.of(gameBoard.getPlayer1(), gameBoard.getPlayer2()));
        pitsRepository.saveAll(gameBoard.getMancalaPits());
        return mancalaGameRepository.save(gameBoard);
    }
}
