
package com.shashi.bol.mancala.game.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.StringJoiner;

@Entity
@Table(name = "MANCALA_PIT")
public class MancalaPit implements Serializable {

    private static final long serialVersionUID = -7944618550900065107L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MANCALA_PIT")
    @SequenceGenerator(name = "SEQ_MANCALA_PIT", sequenceName = "SEQ_MANCALA_PIT", allocationSize = 1)
    private Long id;

    private int pitLocation;
    private Integer stones;

    @ManyToOne
    @JoinColumn(name = "GAME_ID", referencedColumnName = "ID")
    @JsonIgnore
    private MancalaGame game;

    public MancalaPit(int value, int stonesPerPit) {
        this.pitLocation=value;
        this.stones=stonesPerPit;
    }

    public MancalaPit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPitLocation() {
        return pitLocation;
    }

    public void setPitLocation(int pitLocation) {
        this.pitLocation = pitLocation;
    }

    public Integer getStones() {
        return stones;
    }

    public void setStones(Integer stones) {
        this.stones = stones;
    }

    public MancalaGame getGame() {
        return game;
    }

    public void setGame(MancalaGame game) {
        this.game = game;
    }

    @JsonIgnore
    public Boolean isEmpty (){
        return this.stones == 0;
    }

    public void clear (){
        this.stones = 0;
    }

    public void update() {
        this.stones += 1;
    }

    public void addStones (Integer stones){
        this.stones+= stones;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("pitLocation=" + pitLocation)
                .add("stones=" + stones)
                .toString();
    }
}
