package com.shashi.bol.mancala.game.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.domain.MancalaPit;
import com.shashi.bol.mancala.game.v1.domain.Player;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDTO implements Serializable {
    private static final long serialVersionUID = 5664074898516989734L;

    private String gameId;
    private List<MancalaPit> mancalaPits;
    private Player player1;
    private Player player2;
    private int currentPitIndex;
    private MancalaGame.GameStatus gameStatus;

    public GameDTO() {
    }

    public GameDTO(MancalaGame gameBoard) {
        this.gameId = gameBoard.getGameId();
        this.mancalaPits = gameBoard.getMancalaPits();
        this.player1 = gameBoard.getPlayer1();
        this.player2 = gameBoard.getPlayer2();
        this.gameStatus = gameBoard.getGameStatus();
        this.currentPitIndex = gameBoard.getCurrentPitIndex();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<MancalaPit> getMancalaPits() {
        return mancalaPits;
    }

    public void setMancalaPits(List<MancalaPit> mancalaPits) {
        this.mancalaPits = mancalaPits;
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

    public MancalaGame.GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getCurrentPitIndex() {
        return currentPitIndex;
    }

    public void setCurrentPitIndex(int currentPitIndex) {
        this.currentPitIndex = currentPitIndex;
    }

    public void setGameStatus(MancalaGame.GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
