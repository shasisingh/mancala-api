
package com.shashi.bol.mancala.web.game.v1.model;

import java.io.Serializable;



public class MancalaPit implements Serializable {

    private static final long serialVersionUID = -6929628794157365588L;

    private Integer pitLocation;
    private Integer stones;

    public Integer getPitLocation() {
        return pitLocation;
    }

    public MancalaPit() {
    }

    public MancalaPit(Integer pitLocation, Integer stones) {
        this.pitLocation = pitLocation;
        this.stones = stones;
    }

    public void setPitLocation(Integer pitLocation) {
        this.pitLocation = pitLocation;
    }

    public Integer getStones() {
        return stones;
    }

    public void setStones(Integer stones) {
        this.stones = stones;
    }

    public Boolean isEmpty (){
        return this.stones == 0;
    }

    @Override
    public String toString() {
        return  pitLocation.toString() +
                ":" +
                stones.toString() ;
    }
}
