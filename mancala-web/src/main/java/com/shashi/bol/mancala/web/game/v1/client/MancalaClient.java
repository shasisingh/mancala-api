package com.shashi.bol.mancala.web.game.v1.client;

import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;

import static org.springframework.http.HttpMethod.PUT;

@Component
public class MancalaClient implements Serializable {

    private static final long serialVersionUID = -6933792699360352013L;

    private final static Logger LOGGER = LoggerFactory.getLogger(MancalaClient.class);
    private final RestTemplate restTemplate;
    private final MancalaUriBuilder mancalaUriBuilder;

    public MancalaClient(RestTemplate restTemplate, MancalaUriBuilder mancalaUriBuilder) {
        this.restTemplate = restTemplate;
        this.mancalaUriBuilder = mancalaUriBuilder;
    }

    public MancalaGameBoard startNewMancalaGame() {
        URI url = mancalaUriBuilder.getUrlForCreateGame();

        LOGGER.debug("calling:{}",url);

        ResponseEntity<MancalaGameBoard> gameResponse = this.restTemplate.postForEntity(url, null, MancalaGameBoard.class);

        return gameResponse.getBody();
    }

    public MancalaGameBoard startNewMancalaGame(String player1,String player2) {
        URI url = mancalaUriBuilder.getUrlForCreateGameWithPlayersName(player1,player2);
        LOGGER.info("calling:{}", url);
        ResponseEntity<MancalaGameBoard> gameResponse = this.restTemplate.postForEntity(url, null, MancalaGameBoard.class);

        return gameResponse.getBody();
    }

    public MancalaGameBoard moveTheBoard(String gameId, Integer pitIndex) {
        URI url = mancalaUriBuilder.getGamePlayUrl(gameId, pitIndex);

        LOGGER.debug("calling: {}", url);
        ResponseEntity<MancalaGameBoard> response = restTemplate.exchange(url, PUT, null, MancalaGameBoard.class);

        return response.getBody();
    }

}
