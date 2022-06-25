package com.shashi.bol.mancala.game.v1.factory;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.domain.Player;
import com.shashi.bol.mancala.game.v1.repository.MancalaGameRepository;
import com.shashi.bol.mancala.game.v1.repository.PitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GameFactoryTest {


    @InjectMocks
    private GameFactory factory;

    @Mock
    private  MancalaGameRepository mancalaGameRepository;

    @Mock
    private PitRepository pitRepository;

    @Mock
    private PlayerFactory playerFactory;

    @BeforeEach
    public void setup(){
        when(playerFactory.create(any()))
                .thenAnswer( invocationOnMock -> getPlayer(invocationOnMock.getArgument(0)))
                .thenAnswer( invocationOnMock -> getPlayer(invocationOnMock.getArgument(0)));

        when(pitRepository.saveAll(anyList()))
                .thenAnswer( invocationOnMock -> invocationOnMock.getArgument(0));

        when(mancalaGameRepository.save(any()))
                .thenAnswer( invocationOnMock -> invocationOnMock.getArgument(0));
    }

    @Test
     void createWithAllDefault() {
        MancalaGame createPlayer = factory.create();
        assertNotNull(createPlayer);
        assertThat(createPlayer.getGameStatus(),is(MancalaGame.GameStatus.INITIATED));
        assertThat(createPlayer.getGameId(),notNullValue());
        assertThat(createPlayer.getMancalaPits().size(),is(14));
        assertThat(createPlayer.getPlayer1().getName(),is("Wolverine"));
        assertThat(createPlayer.getPlayer2().getName(),is("Deadpool"));

        createPlayer.getMancalaPits().forEach(var1 -> {
            assertThat(var1.getGame(), is(createPlayer));
            if (var1.getPitLocation() == 7 || var1.getPitLocation() == 14) {
                assertThat("checking for location" + var1.getPitLocation(), var1.getStones(), is(0));
            } else {
                assertThat("checking for location" + var1.getPitLocation(), var1.getStones(), is(6));
            }
        });

        verify(playerFactory,times(2)).create(any(String.class));
        verify(pitRepository).saveAll(anyList());
        verify(mancalaGameRepository).save(any(MancalaGame.class));
    }

    @Test
     void testCreateWithSuppliedStones() {
        MancalaGame createPlayer = factory.create(4);
        assertNotNull(createPlayer);
        assertThat(createPlayer.getGameStatus(),is(MancalaGame.GameStatus.INITIATED));
        assertThat(createPlayer.getGameId(),notNullValue());
        assertThat(createPlayer.getMancalaPits().size(),is(14));
        assertThat(createPlayer.getPlayer1().getName(),is("Wolverine"));
        assertThat(createPlayer.getPlayer2().getName(),is("Deadpool"));

        createPlayer.getMancalaPits().forEach(var1 -> {
            assertThat(var1.getGame(), is(createPlayer));
            if (var1.getPitLocation() == 7 || var1.getPitLocation() == 14) {
                assertThat("checking for location" + var1.getPitLocation(), var1.getStones(), is(0));
            } else {
                assertThat("checking for location" + var1.getPitLocation(), var1.getStones(), is(4));
            }
        });

        verify(playerFactory,times(2)).create(any(String.class));
        verify(pitRepository).saveAll(anyList());
        verify(mancalaGameRepository).save(any(MancalaGame.class));
    }

    @Test
     void testCreateSuppliedPlayer() {

        MancalaGame createPlayer = factory.create(4,"A","B");
        assertNotNull(createPlayer);
        assertThat(createPlayer.getGameStatus(),is(MancalaGame.GameStatus.INITIATED));
        assertThat(createPlayer.getGameId(),notNullValue());
        assertThat(createPlayer.getMancalaPits().size(),is(14));
        assertThat(createPlayer.getPlayer1().getName(),is("A"));
        assertThat(createPlayer.getPlayer2().getName(),is("B"));

        createPlayer.getMancalaPits().forEach(var1 -> {
            assertThat(var1.getGame(), is(createPlayer));
            if (var1.getPitLocation() == 7 || var1.getPitLocation() == 14) {
                assertThat("checking for location" + var1.getPitLocation(), var1.getStones(), is(0));
            } else {
                assertThat("checking for location" + var1.getPitLocation(), var1.getStones(), is(4));
            }
        });

        verify(playerFactory,times(2)).create(any(String.class));
        verify(pitRepository).saveAll(anyList());
        verify(mancalaGameRepository).save(any(MancalaGame.class));
    }

    private Player getPlayer(String player1){
        Player player= new Player();
        player.setName(player1);
        return player;
    }


}
