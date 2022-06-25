package com.shashi.bol.mancala.web.game.v1.client;

import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MancalaClientTest {

    @InjectMocks
    MancalaClient mancalaClient;

    @Mock
    RestTemplate restTemplate;

    @Mock MancalaUriBuilder mancalaUriBuilder;

    @Test
    void testCreateGame() throws URISyntaxException {
        when(mancalaUriBuilder.getUrlForCreateGame()).thenReturn(new URI("http://myUrl-to-be-test"));
        when(restTemplate.postForEntity(any(),any(),any()))
                .thenReturn(ResponseEntity.ok(new MancalaGameBoard()));
        MancalaGameBoard res = mancalaClient.startNewMancalaGame();
        assertNotNull(res);
    }

    @Test
    void testCreateGameWithPlayer() throws URISyntaxException {
        when(mancalaUriBuilder.getUrlForCreateGameWithPlayersName(any(),any())).thenReturn(new URI("http://myUrl-to-be-test"));
        when(restTemplate.postForEntity(any(),any(),any()))
                .thenReturn(ResponseEntity.ok(new MancalaGameBoard()));
        MancalaGameBoard res = mancalaClient.startNewMancalaGame("player1","player2");
        assertNotNull(res);
        verify(mancalaUriBuilder).getUrlForCreateGameWithPlayersName("player1","player2");
    }

    @Test
    void testPlayGame() throws URISyntaxException {
        when(mancalaUriBuilder.getGamePlayUrl(any(),anyInt()))
                .thenReturn(new URI("http://myUrl-to-be-test1?gameId:1"));


        ResponseEntity responseEntity = new ResponseEntity(new MancalaGameBoard(), HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.any(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(responseEntity);

        MancalaGameBoard res = mancalaClient.moveTheBoard("gameId",1);
        assertNotNull(res);
        verify(mancalaUriBuilder).getGamePlayUrl("gameId",1);
    }


}
