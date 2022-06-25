package com.shashi.bol.mancala.web.game.v1.client;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;


class MancalaUriBuilderTest {

    @Test
     void playGameUrl() throws URISyntaxException {
        MancalaUriBuilder mancalaUriBuilder = new MancalaUriBuilder(new URI("https://test.org"));
        assertEquals( new URI("https://test.org/play/gameId/pits/1"), mancalaUriBuilder.getGamePlayUrl("gameId", 1) );
    }

    @Test
     void createGameWithPlayer() throws URISyntaxException {
        MancalaUriBuilder mancalaUriBuilder = new MancalaUriBuilder(new URI("https://test1.org"));
        assertEquals( new URI("https://test1.org/create?player1=player1&player2=player2"), mancalaUriBuilder.getUrlForCreateGameWithPlayersName("player1","player2") );
    }

    @Test
     void createGame() throws URISyntaxException {
        MancalaUriBuilder mancalaUriBuilder = new MancalaUriBuilder(new URI("https://test1.org"));
        assertEquals( new URI("https://test1.org/create"), mancalaUriBuilder.getUrlForCreateGame() );
    }


}
