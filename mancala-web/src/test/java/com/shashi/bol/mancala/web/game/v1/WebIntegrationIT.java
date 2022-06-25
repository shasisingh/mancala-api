package com.shashi.bol.mancala.web.game.v1;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.shashi.bol.mancala.web.game.v1.client.MancalaClient;
import com.shashi.bol.mancala.web.game.v1.model.MancalaGameBoard;
import com.shashi.bol.mancala.web.game.v1.model.MancalaPit;
import org.awaitility.Durations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;

@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(port = 1)
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("WEB:testing container:as we dont have implementation for html")
class WebIntegrationIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    Environment env;

    @Autowired
    private WebApplicationContext wac;


    @Test
    @Order(value=0)
     void shouldPopulateEnvironmentWithWiremockPort() {
        assertThat(env.containsProperty("wiremock.server.port")).isTrue();
        assertThat(env.getProperty("wiremock.server.port")).matches("\\d+");
    }


    @Test
    @Order(value=1)
    @DisplayName("Making sure application is UP and running. before test start")
     void waitTestToAlive() {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(Durations.TWO_HUNDRED_MILLISECONDS)
                .untilAsserted(() -> {
                    given()
                            .when()
                            .baseUri("http://localhost:"+port).get("/actuator/health")
                            .then()
                            .log().all()
                            .statusCode(200)
                            .body("status",is("UP"));
                });
    }

    @Test
    @Order(value=2)
    @DisplayName("Requesting API to create new game:Default")
     void createNewGame() throws IOException {
        stubFor(WireMock.post(urlMatching("/games/v1/mancala/create"))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(asJson("mancala-creation.json"))
                ));

        MancalaClient client = wac.getBean(MancalaClient.class);
        MancalaGameBoard mancalaGame = client.startNewMancalaGame();

        List<MancalaPit> mancalaPits = Arrays.asList(
                new MancalaPit(1 , 6),
                new MancalaPit(2 , 6),
                new MancalaPit(3 , 6),
                new MancalaPit(4 , 6),
                new MancalaPit(5 , 6),
                new MancalaPit(6 , 6),
                new MancalaPit(7 , 0),
                new MancalaPit(8 , 6),
                new MancalaPit(9 , 6),
                new MancalaPit(10 , 6),
                new MancalaPit(11 , 6),
                new MancalaPit(12 , 6),
                new MancalaPit(13 , 6),
                new MancalaPit(14 , 0));

        then(mancalaGame.getPlayer1().getName()).isEqualTo("Wolverine");
        then(mancalaGame.getPlayer2().getName()).isEqualTo("Deadpool");
        then(mancalaGame.getMancalaPits().size()).isEqualTo(mancalaPits.size());
        then(mancalaGame.leftHouseStones()).isEqualTo(0);
        then(mancalaGame.rightHouseStones()).isEqualTo(0);
    }

    @Test
    @Order(value=3)
    @DisplayName("Requesting API to create new game:With Players info")
    void createNewGameWithPlayers() throws IOException {
        stubFor(WireMock.post(urlMatching("/games/v1/mancala/create\\?player1=superMan&player2=X-man"))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(asJson("mancala-creation-withplayer-name.json"))
                ));

        MancalaClient client = wac.getBean(MancalaClient.class);
        MancalaGameBoard mancalaGame = client.startNewMancalaGame("superMan","X-man");

        List<MancalaPit> mancalaPits = Arrays.asList(
                new MancalaPit(1 , 6),
                new MancalaPit(2 , 6),
                new MancalaPit(3 , 6),
                new MancalaPit(4 , 6),
                new MancalaPit(5 , 6),
                new MancalaPit(6 , 6),
                new MancalaPit(7 , 0),
                new MancalaPit(8 , 6),
                new MancalaPit(9 , 6),
                new MancalaPit(10 , 6),
                new MancalaPit(11 , 6),
                new MancalaPit(12 , 6),
                new MancalaPit(13 , 6),
                new MancalaPit(14 , 0));

        then(mancalaGame.getPlayer1().getName()).isEqualTo("SuperMan");
        then(mancalaGame.getPlayer2().getName()).isEqualTo("X-man");
        then(mancalaGame.getMancalaPits().size()).isEqualTo(mancalaPits.size());
        then(mancalaGame.leftHouseStones()).isEqualTo(0);
        then(mancalaGame.rightHouseStones()).isEqualTo(0);
    }

    @Test
    @Order(value=4)
    @DisplayName("Requesting API play a game with requested pit.")
    void playGame() throws IOException {
        String gameId = UUID.randomUUID().toString();
        stubFor(WireMock.put(urlMatching("/games/v1/mancala/play/"+gameId+"/pits/2"))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(asJson("mancala-play-2.json"))
                ));

        MancalaClient client = wac.getBean(MancalaClient.class);
        MancalaGameBoard mancalaGame = client.moveTheBoard(gameId,2);

        List<MancalaPit> mancalaPits = Arrays.asList(
                new MancalaPit(1 , 6),
                new MancalaPit(2 , 0),
                new MancalaPit(3 , 7),
                new MancalaPit(4 , 7),
                new MancalaPit(5 , 7),
                new MancalaPit(6 , 7),
                new MancalaPit(7 , 1),
                new MancalaPit(8 , 7),
                new MancalaPit(9 , 6),
                new MancalaPit(10 , 6),
                new MancalaPit(11 , 6),
                new MancalaPit(12 , 6),
                new MancalaPit(13 , 6),
                new MancalaPit(14 , 0));

        then(mancalaGame.getPlayer1().getName()).isEqualTo("Wolverine");
        then(mancalaGame.getPlayer2().getName()).isEqualTo("Deadpool");
        then(mancalaGame.getMancalaPits().size()).isEqualTo(mancalaPits.size());
        then(mancalaGame.leftHouseStones()).isEqualTo(0);
        then(mancalaGame.rightHouseStones()).isEqualTo(1);
        assertThat(mancalaGame.getMancalaPits()
                .stream().map(MancalaPit::getStones)
                .collect(Collectors.toList()))
                .isEqualTo(mancalaPits.stream().map(MancalaPit::getStones)
                        .collect(Collectors.toList()));
    }

    private String asJson(String resource) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource(resource).getInputStream(), Charset.defaultCharset());
    }

}
