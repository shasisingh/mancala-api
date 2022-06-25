
package com.shashi.bol.mancala.web.game.v1.controller;

import com.shashi.bol.mancala.web.game.v1.events.DisplayGameEvent;
import com.shashi.bol.mancala.web.game.v1.exceptions.ApiConnectionException;
import com.shashi.bol.mancala.web.game.v1.client.MancalaClient;
import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.FIRST_PIT_PLAYER_B;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.LEFT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.RIGHT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.web.game.v1.constants.MancalaConstants.SIXTH_PIT_PLAYER_A;

/**
 * The type Game controller.
 */
/*
    This class acts as Controller for the web application
 */
@Component
public class GameController implements Serializable {
    private static final long serialVersionUID = 2405172041941251807L;

    private MancalaGameBoard game;
    private final MancalaClient mancalaClient;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Instantiates a new Game controller.
     *
     * @param mancalaClient  the mancala client
     * @param eventPublisher the event publisher
     */
    public GameController(MancalaClient mancalaClient, ApplicationEventPublisher eventPublisher) {
        this.mancalaClient = mancalaClient;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Start new game mancala game board.
     *
     * @return the mancala game board
     * @throws ApiConnectionException the api connection exception
     */
    public MancalaGameBoard startNewGame() throws ApiConnectionException {
        try {
            this.game = mancalaClient.startNewMancalaGame();
            return this.game;
        } catch (Exception ex) {
            throw new ApiConnectionException("Error connecting to Mancala service!", ex);
        }
    }

    /**
     * Start new game mancala game board.
     *
     * @param player1 the player 1
     * @param player2 the player 2
     * @return the mancala game board
     * @throws ApiConnectionException the api connection exception
     */
    public MancalaGameBoard startNewGame(String player1, String player2) throws ApiConnectionException {
        try {
            this.game = mancalaClient.startNewMancalaGame(player1, player2);
            return this.game;
        } catch (Exception ex) {
            throw new ApiConnectionException("Error connecting to Mancala service!", ex);
        }
    }

    /**
     * Apply event.
     *
     * @param pitIndex the pit index
     * @throws ApiConnectionException the api connection exception
     */
    public void applyEvent(Integer pitIndex) throws ApiConnectionException {
        try {
            if (game == null) {
                throw new ApiConnectionException("Please click on 'Start Game' button to start the game first!");
            }
            if(isValidMove(pitIndex)) {
                this.game = mancalaClient.moveTheBoard(this.game.getGameId(), pitIndex);
                this.eventPublisher.publishEvent(new DisplayGameEvent(this, this.game, pitIndex));
                return;
            }
            Notification.show("Invalid pit.",2000, Notification.Position.MIDDLE);
        } catch (Exception ex){
            throw new ApiConnectionException("Error connecting to Mancala service!", ex);
        }
    }

    /**
     * Has game started boolean.
     *
     * @return the boolean
     */
    public boolean hasGameStarted () {
        return this.game != null;
    }

    /**
     * Reset.
     */
    public void reset(){
        game=null;
    }

    private boolean isValidMove(int selectedIndex) {
        if (game.isEmptyPit(selectedIndex) || selectedIndex == LEFT_PIT_HOUSE_ID || selectedIndex == RIGHT_PIT_HOUSE_ID) {
            return false;
        } else if ((!game.getPlayer1().isTurn() && !game.getPlayer2().isTurn())) {
            return true;
        } else if (game.getPlayer1().isTurn() && selectedIndex > SIXTH_PIT_PLAYER_A) {
            return false;
        } else {
            return !(game.getPlayer2().isTurn() && selectedIndex < FIRST_PIT_PLAYER_B);
        }
    }
}

