package com.shashi.bol.mancala.game.v1;

import com.shashi.bol.mancala.game.v1.domain.MancalaPit;
import com.shashi.bol.mancala.game.v1.repository.MancalaGameRepository;
import com.shashi.bol.mancala.game.v1.repository.PitRepository;
import com.shashi.bol.mancala.game.v1.repository.PlayerRepository;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@DisplayName("[SECURE]:Testing mancala-api:")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiIntegrationIT {

    private final String HOST ="https://localhost:";

    private PitRepository pitRepository;
    private PlayerRepository playerRepository;
    private MancalaGameRepository mancalaGameRepository;

    private static String pathToClientTrustStore;
    private static String pathToClientKeyStore;

    @LocalServerPort
    private Integer port;


    @Autowired
    Environment environment;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    public void setup() {
        pathToClientKeyStore = context.getEnvironment().getProperty("client.ssl.keystore.filename");
        pathToClientTrustStore = context.getEnvironment().getProperty("client.ssl.truststore.filename");
        pitRepository = context.getBean(PitRepository.class);
        playerRepository = context.getBean(PlayerRepository.class);
        mancalaGameRepository = context.getBean(MancalaGameRepository.class);
    }

    @Test
    @DisplayName("START GAME:new game is created with default pits,default pits:6")
    void shouldCreateNewGame() {
        given()
                .when()
                .keyStore(pathToClientKeyStore, "vt56@612")
                .trustStore(pathToClientTrustStore, "vt56@612")
                .baseUri(HOST + port).post("/games/v1/mancala/create")
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .body("mancalaPits.size()", is(14))
                .body("player1.name", is("Wolverine"))
                .body("player1.turn", is(false))
                .body("player2.name", is("Deadpool"))
                .body("player2.turn", is(false))
                .body("gameStatus", is("INITIATED"));
    }

    @Test
    @DisplayName("Openapi:verify if api is been exposed")
    void openApi_must_be_exposed() {
        given()
                .when()
                .keyStore(pathToClientKeyStore, "vt56@612")
                .trustStore(pathToClientTrustStore, "vt56@612")
                .baseUri(HOST + port)
                .get("v3/api-docs")
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Openapi:verify if api-gui is been exposed")
    void openApi_gui_must_be_exposed() {
        given()
                .when()
                .keyStore(pathToClientKeyStore, "vt56@612")
                .trustStore(pathToClientTrustStore, "vt56@612")
                .baseUri(HOST + port)
                .get("/swagger-ui/index.html")
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .and()
                .contentType(ContentType.HTML);
    }



    @Test
    @DisplayName("Https:START GAME:new game is created with default pits,default pits:6")
    void shouldCreateNewGameWithHttps() {
        given()
                .when()
                .keyStore(pathToClientKeyStore, "vt56@612")
                .trustStore(pathToClientTrustStore, "vt56@612")
                .baseUri(HOST + port).post("/games/v1/mancala/create")
                .then()
                .log().ifError()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .body("mancalaPits.size()", is(14))
                .body("player1.name", is("Wolverine"))
                .body("player1.turn", is(false))
                .body("player2.name", is("Deadpool"))
                .body("player2.turn", is(false))
                .body("gameStatus", is("INITIATED"));
    }
    @Test
    @DisplayName("START GAME:new game is created with:specified pits [4]")
    void shouldCreateNewGameWithPitsAndPlayer() {
        given()
                .when()
                .param("pitStone", "4")
                .param("player1", "player1")
                .param("player2", "player2")
                .keyStore(pathToClientKeyStore, "vt56@612")
                .trustStore(pathToClientTrustStore, "vt56@612")
                .baseUri(HOST + port).post("/games/v1/mancala/create")
                .then()
                .log().ifError()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .body("mancalaPits.size()", is(14))
                .body("mancalaPits[0].pitLocation", is(1))
                .body("mancalaPits[0].stones", is(4))
                .body("mancalaPits[6].pitLocation", is(7))
                .body("mancalaPits[6].stones", is(0))
                .body("player1.name", is("player1"))
                .body("player1.turn", is(false))
                .body("player2.name", is("player2"))
                .body("player2.turn", is(false))
                .body("gameStatus", is("INITIATED"));
    }

    @Test
    @DisplayName("STRATEGY-1: Get 6 Stones in your Mancala before the opponent gets his turn")
    void strategy1_expectedIsPlayer1_to_collect_6_stones(){
     //gameset:
     //:-> player 2 :  0  0 3 0 1 0 : Home pit : 0
     //:   player 1 :  0  1 0 3 0 0 : Home pit : 0

        ExtractableResponse<Response> response =
                given().when()
                        .keyStore(pathToClientKeyStore, "vt56@612")
                        .trustStore(pathToClientTrustStore, "vt56@612")
                        .baseUri(HOST + port)
                        .post("/games/v1/mancala/create")
                        .then()
                        .log()
                        .ifError().statusCode(200)
                        .extract();

        final String gameId = response.jsonPath().get("gameId");

        updateGameWithGameId(gameId,gameStrategy1(),true,false); // updated with new and required strategy so we can play game.

        ////calling play-api to make a move;
        getMoveAndExpected(1).forEach((key, value) -> {
            ExtractableResponse<Response> newResponse = given()
                    .when()
                    .keyStore(pathToClientKeyStore, "vt56@612")
                    .trustStore(pathToClientTrustStore, "vt56@612")
                    .baseUri(HOST + port).put("/games/v1/mancala/play/" + gameId + "/pits/" + key)
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .extract();

            assertResponse(value, newResponse);
        });


        //calling status-api to verify- game status
        given()
                .when()
                .keyStore(pathToClientKeyStore, "vt56@612")
                .trustStore(pathToClientTrustStore, "vt56@612")
                .baseUri(HOST + port).get("/games/v1/mancala/play/status/" + gameId)
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .body("gameStatus",is("INITIATED"))
                .body("player1.turn",is(false))
                .body("player2.turn",is(true));



    }

    @Test
    @DisplayName("STRATEGY-2:WINNER MOVE::checking with move 6 from player1:player2 is winner")
    void strategy2_expectedIsPlayer2_move_player2_winner(){
        //gameset:
        //:-> player 2 :  2  2 10 3 2 13 : Home pit : 4
        //:   player 1 :  0  0 0 0 0 1 : Home pit : 12

        ExtractableResponse<Response> response =
                given().when()
                        .keyStore(pathToClientKeyStore, "vt56@612")
                        .trustStore(pathToClientTrustStore, "vt56@612")
                        .baseUri(HOST + port)
                        .post("/games/v1/mancala/create")
                        .then()
                        .log()
                        .ifError().statusCode(200)
                        .extract();

        final String gameId = response.jsonPath().get("gameId");

        updateGameWithGameId(gameId,gameStrategy2(),true,false); // updated with new and required strategy so we can play game.

        ////calling play-api to make a move;
        getMoveAndExpected(2).forEach((key, value) -> {
            ExtractableResponse<Response> newResponse = given()
                    .when()
                    .keyStore(pathToClientKeyStore, "vt56@612")
                    .trustStore(pathToClientTrustStore, "vt56@612")
                    .baseUri(HOST + port).put("/games/v1/mancala/play/" + gameId + "/pits/" + key)
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .extract();

            assertResponse(value, newResponse);
        });


        //calling status-api to verify- game status
        given()
                .when()
                .keyStore(pathToClientKeyStore, "vt56@612")
                .trustStore(pathToClientTrustStore, "vt56@612")
                .baseUri(HOST + port).get("/games/v1/mancala/play/status/" + gameId)
                .then()
                .log()
                .ifError()
                .statusCode(200)
                .body("gameStatus",is("FINISHED"))
                .body("player1.name",is("Wolverine"))
                .body("player2.turn",is(false))
                .body("player2.name",is("Deadpool"))
                .body("player2.status",is("WINNER"));



    }

    private void assertResponse(String value, ExtractableResponse<Response> newResponse) {
        List<Integer> actualStone = newResponse.jsonPath().getList("mancalaPits", MancalaPit.class)
                .stream()
                .map(MancalaPit::getStones)
                .collect(Collectors.toList());
        validatePits(value,actualStone);
    }


    private Map<String, String> getMoveAndExpected(int strategy) {
        HashMap<String, String> strategyMap = new LinkedHashMap<>();
        if (strategy == 1) {
            strategyMap.put("4", "0,1,0,0,1,1,1,0,1,0,3,0,0,0");//player1 to player2 -> right ot left
            strategyMap.put("6", "0,1,0,0,1,0,2,0,1,0,3,0,0,0");//player1 to player2 -> right ot left
            strategyMap.put("2", "0,0,0,0,1,0,6,0,1,0,0,0,0,0");//player1 to player2 -> right ot left
        } else if (strategy == 2) {
            strategyMap.put("6", "0,0,0,0,0,0,13,0,0,0,0,0,0,36");//player1 to player2 -> right ot left
        }
        return strategyMap;
    }

    void validatePits(String expectedValues,List<Integer> actual ) {
        AtomicInteger counter = new AtomicInteger(0);
        Arrays.stream(expectedValues.split(","))
                .forEach( expected -> {
                assertThat("value@pit-location:" + counter.get() + ", not as expected", actual.get(counter.get()).toString(), is(expected));
                counter.set(counter.get() + 1);
        });

    }

    private List<Integer> gameStrategy1() {
        return List.of(0, 1, 0, 3, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0);
    }

    private List<Integer> gameStrategy2() {
        return List.of(0, 0, 0, 0, 0, 1, 12, 2, 2, 10, 3, 2, 13, 4);
    }

    public void updateGameWithGameId(String gameId,List<Integer> newGameSet, boolean player1Turn, boolean player2Turn){
        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        AtomicInteger pitLocation = new AtomicInteger(1);
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    mancalaGameRepository.findByGameId(gameId)
                            .ifPresent(game -> {
                                game.setPitsMoved(true);
                                newGameSet.forEach( newValue -> game.getPit(pitLocation.getAndIncrement()).setStones(newValue));
                                pitRepository.saveAllAndFlush(game.getMancalaPits());
                                game.getPlayer1().setTurn(player1Turn);
                                game.getPlayer2().setTurn(player2Turn);
                                playerRepository.saveAllAndFlush(List.of(game.getPlayer1(),game.getPlayer2()));
                                mancalaGameRepository.saveAndFlush(game);
                            });
                }});


    }

}
