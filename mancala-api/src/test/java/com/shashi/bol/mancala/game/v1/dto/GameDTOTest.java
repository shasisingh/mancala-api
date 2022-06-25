package com.shashi.bol.mancala.game.v1.dto;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.domain.Player;
import com.shashi.bol.mancala.game.v1.model.GameDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameDTOTest {

    @Test
     void testAccountProfile() {
        assertThat(GameDTO.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()));
    }

    @Test
     void testDto(){
        MancalaGame game = new MancalaGame();
        game.setGameId("gameId");
        game.setPlayer1(new Player());
        game.setPlayer2(new Player());
        game.setMancalaPits(List.of());
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        GameDTO gameDto = new GameDTO(game);

        assertAll("verify dto",

                () -> assertEquals("gameId", gameDto.getGameId()),
                () -> assertEquals(MancalaGame.GameStatus.INITIATED, gameDto.getGameStatus()),
                () -> assertTrue(gameDto.getMancalaPits().isEmpty()),
                () -> assertNotNull(gameDto.getPlayer1()),
                () -> assertNotNull(gameDto.getPlayer2())

        );
    }
}
