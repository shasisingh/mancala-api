package com.shashi.bol.mancala.web.game.v1.client;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;

public class MancalaUriBuilder implements Serializable {

    private static final long serialVersionUID = -7664453307608153763L;

    private final UriComponentsBuilder urlCreateNewGame;
    private final UriComponentsBuilder playGameUrl;

    public MancalaUriBuilder(URI baseUrl) {
        this.urlCreateNewGame = UriComponentsBuilder.fromUri(baseUrl).path("/create");
        this.playGameUrl = UriComponentsBuilder.fromUri(baseUrl).path("/play/{gameId}/pits/{fromPitId}");
    }

    public URI getUrlForCreateGame() {
        return urlCreateNewGame.build().toUri();
    }

    public URI getUrlForCreateGameWithPlayersName(final String player1,final String player2) {
        urlCreateNewGame.replaceQueryParams(getQueryParams(player1,player2));
        return urlCreateNewGame.build().toUri();
    }

    public URI getGamePlayUrl(final String gameId,final int fromPitId) {
        return playGameUrl.build(gameId,fromPitId);
    }

    private MultiValueMap<String, String> getQueryParams(String player1, String player2) {
        MultiValueMap<String, String> queryParamsMap = new LinkedMultiValueMap<>();
        queryParamsMap.add("player1", player1);
        queryParamsMap.add("player2", player2);
        return queryParamsMap;
    }

}
