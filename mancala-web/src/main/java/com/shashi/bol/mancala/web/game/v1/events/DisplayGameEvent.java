
package com.shashi.bol.mancala.web.game.v1.events;

import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;
import org.springframework.context.ApplicationEvent;

/**
 * This event is fired when user clicks on any pit to sow the game. As a result of this event, a call is made to
 *
 * Mancala Api and the application is filled with the results of sowing the pit for selected index
 */

public class DisplayGameEvent extends ApplicationEvent {


    private MancalaGameBoard game;
    private Integer pitIndex;
    public DisplayGameEvent(Object source, MancalaGameBoard game, Integer pitIndex) {
        super(source);
        this.game = game;
        this.pitIndex = pitIndex;
    }
    public MancalaGameBoard getGame() {
        return game;
    }

    public void setGame(MancalaGameBoard game) {
        this.game = game;
    }

    public Integer getPitIndex() {
        return pitIndex;
    }

    public void setPitIndex(Integer pitIndex) {
        this.pitIndex = pitIndex;
    }
}
