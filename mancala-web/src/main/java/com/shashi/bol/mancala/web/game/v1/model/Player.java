package com.shashi.bol.mancala.web.game.v1.model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = -7587345938539922707L;

    private String name;
    private boolean turn;
    private boolean prevTurn;
    private String status;

    public boolean playerGetFreeTurn(){
        return this.prevTurn && this.turn ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isPrevTurn() {
        return prevTurn;
    }

    public void setPrevTurn(boolean prevTurn) {
        this.prevTurn = prevTurn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
