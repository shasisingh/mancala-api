package com.shashi.bol.mancala.game.v1.api;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;

public interface MancalaBoardApi {

    MancalaGame makeMove(String gameId, int index);

    boolean isEmpty(int index);

    boolean isGameOver();

    boolean isValidMove(int selectedPit);

    MancalaGame.GameStatus getCurrentStatus();

    int getTotalStonesFromPlayer1();

    int getTotalStonesFromPlayer2();

    boolean isHomePit();
}
