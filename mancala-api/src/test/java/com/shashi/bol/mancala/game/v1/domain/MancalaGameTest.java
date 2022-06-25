package com.shashi.bol.mancala.game.v1.domain;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


class MancalaGameTest {

    @Test
     void testAccountProfile() {
        assertThat(MancalaGame.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters()));
    }

    @Test
     void testToStringAndIsPitsMoved(){
        MancalaGame mancalaGame= new MancalaGame();
        mancalaGame.setGameStatus(MancalaGame.GameStatus.INITIATED);
        mancalaGame.setMancalaPits(List.of());
        mancalaGame.setPlayer1(getPlayer());
        mancalaGame.setPlayer2(getPlayer());
        mancalaGame.setPitsMoved(true);
        assertThat(mancalaGame.isPitsMoved(),is(true));
        assertThat("checkingToString",mancalaGame.toString(),is("id=null, gameId='null', player1=test, player2=test, currentPitIndex=0, pitsMoved=true, gameStatus=INITIATED"));
        mancalaGame.setPitsMoved(false);
        assertThat(mancalaGame.isPitsMoved(),is(false));
        assertThat("checkingToString",mancalaGame.toString(),is("id=null, gameId='null', player1=test, player2=test, currentPitIndex=0, pitsMoved=false, gameStatus=INITIATED"));
        mancalaGame.setPitsMoved(true);
        assertThat("checkingToString",mancalaGame.toString(),is("id=null, gameId='null', player1=test, player2=test, currentPitIndex=0, pitsMoved=true, gameStatus=INITIATED"));

        assertThat(mancalaGame.isPitsMoved(),is(true));
    }

    @Test
     void testException(){
        MancalaGame mancalaGame= new MancalaGame();
        mancalaGame.setMancalaPits(List.of());

        assertThatThrownBy(() -> mancalaGame.getPit(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("reason","Invalid pitIndex:1 has given!")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    private Player getPlayer(){
        Player player= new Player();
        player.setName("test");
        return player;
    }
}
