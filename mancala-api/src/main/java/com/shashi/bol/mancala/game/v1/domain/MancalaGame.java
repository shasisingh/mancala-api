
package com.shashi.bol.mancala.game.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


@Entity
@Table(name = "MANCALA")
public class MancalaGame implements Serializable {

    private static final long serialVersionUID = 7434172045950252807L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACCOUNT")
    @SequenceGenerator(name = "SEQ_MANCALA", sequenceName = "SEQ_MANCALA", allocationSize = 1)
    private Long id;

    private String gameId;

    @OneToMany(mappedBy = "game")
    private List<MancalaPit> mancalaPits= new ArrayList<>();

    @OneToOne
    private Player player1;

    @OneToOne
    private Player player2;

    private int currentPitIndex;

    @JsonIgnore
    private boolean pitsMoved;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getCurrentPitIndex() {
        return currentPitIndex;
    }

    public void setCurrentPitIndex(int currentPitIndex) {
        this.currentPitIndex = currentPitIndex;
    }

    public boolean isPitsMoved() {
        return pitsMoved;
    }

    public void setPitsMoved(boolean anyPitsMoved) {
        this.pitsMoved = anyPitsMoved;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public MancalaPit getPit(Integer pitIndex) {
            return this.mancalaPits.stream().filter(pit -> pit.getPitLocation() == pitIndex)
                    .findAny()
                    .orElseThrow( () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pitIndex:" + pitIndex + " has given!"));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("id=" + id)
                .add("gameId='" + gameId + "'")
                .add("player1=" + player1.getName())
                .add("player2=" + player2.getName())
                .add("currentPitIndex=" + currentPitIndex)
                .add("pitsMoved=" + pitsMoved)
                .add("gameStatus=" + gameStatus)
                .toString();
    }

    public enum GameStatus{
        INITIATED,
        ON_GOING,
        FINISHED,
        TIE,
    }


}
