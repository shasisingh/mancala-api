package com.shashi.bol.mancala.game.v1.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.StringJoiner;

@Entity
@Table(name = "PLAYER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player implements Serializable {

    private static final long serialVersionUID = 4437405639959396493L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLAYER")
    @JsonIgnore
    @SequenceGenerator(name = "SEQ_PLAYER", sequenceName = "SEQ_PLAYER", allocationSize = 1)
    private Long id;

    private String name;
    private boolean turn;
    private boolean prevTurn;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String playerName) {
        this.name = playerName;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("turn=" + turn)
                .add("prevTurn=" + prevTurn)
                .add("status='" + status + "'")
                .toString();
    }
}
