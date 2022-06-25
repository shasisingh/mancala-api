package com.shashi.bol.mancala.game.v1.api;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;

public interface MancalaGameApi {
    MancalaGame createGame(final int stones);

    MancalaGame createGame(final int stones, final String player1, final String player2);
}
