
package com.shashi.bol.mancala.web.game.v1.model;

import com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.List;

import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.LEFT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard.GameStatus.INITIATED;
import static com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard.GameStatus.ON_GOING;

public class MancalaGameBoard implements Serializable {
    private static final long serialVersionUID = 3546564323313480339L;

    private List<MancalaPit> mancalaPits;
    private String gameId;
    private Player player1;
    private Player player2;
    private GameStatus gameStatus;
    private int currentPitIndex;

    public MancalaPit getPit (Integer pitIndex){
        return this.mancalaPits.stream()
                .filter(pit -> pit.getPitLocation().equals(pitIndex))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "error"));
    }

    public List<MancalaPit> getMancalaPits() {
        return mancalaPits;
    }

    public void setMancalaPits(List<MancalaPit> mancalaPits) {
        this.mancalaPits = mancalaPits;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Integer leftHouseStones(){
        return getPit(LEFT_PIT_HOUSE_ID).getStones();
    }

    public Integer rightHouseStones(){
        return getPit(MancalaConstants.RIGHT_PIT_HOUSE_ID).getStones();
    }

    public Integer getPitStones (Integer pitIndex){
        return getPit(pitIndex).getStones();
    }

    public boolean onGoingGame() {
        return INITIATED.equals(gameStatus) || ON_GOING.equals(gameStatus);
    }

    public int getCurrentPitIndex() {
        return currentPitIndex;
    }

    public void setCurrentPitIndex(int currentPitIndex) {
        this.currentPitIndex = currentPitIndex;
    }

    public boolean isEmptyPit(Integer pitLocation) {
        return getPitStones(pitLocation) == 0;
    }

    public enum GameStatus{
        INITIATED,
        ON_GOING,
        FINISHED,
        TIE,
    }
}
