

package com.shashi.bol.mancala.game.v1.service;

import com.shashi.bol.mancala.game.v1.api.MancalaGameApi;
import com.shashi.bol.mancala.game.v1.factory.GameFactory;
import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import org.springframework.stereotype.Service;

@Service
public class MancalaGameService implements MancalaGameApi {

    private final GameFactory gameFactory;

    public MancalaGameService(GameFactory gameFactory) {
        this.gameFactory = gameFactory;
    }

    @Override
    public MancalaGame createGame(int pitStones) {
        return gameFactory.create(pitStones);
    }

    @Override
    public MancalaGame createGame(int stones, final String player1, final String player2) {
        return gameFactory.create(stones, player1, player2);
    }

}
