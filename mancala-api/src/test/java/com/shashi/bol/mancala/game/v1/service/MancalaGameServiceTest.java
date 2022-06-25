package com.shashi.bol.mancala.game.v1.service;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.factory.GameFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
 class MancalaGameServiceTest {

    @InjectMocks
    MancalaGameService service;

    @Mock
    private GameFactory gameFactory;

    @Test
     void createGame() {
        when(gameFactory.create(anyInt())).thenReturn(getGame());
        MancalaGame game = service.createGame(1);
        assertNotNull(game);
        verify(gameFactory).create(1);
    }

    @Test
     void testCreateGame() {
        when(gameFactory.create(anyInt(),any(),any())).thenReturn(getGame());
        MancalaGame game = service.createGame(1,"player1","player2");
        assertNotNull(game);
        verify(gameFactory).create(eq(1),eq("player1"),eq("player2"));
    }
    private MancalaGame getGame(){
        MancalaGame game= new MancalaGame();
        game.setGameId("gameId");
        return game;
    }
}
