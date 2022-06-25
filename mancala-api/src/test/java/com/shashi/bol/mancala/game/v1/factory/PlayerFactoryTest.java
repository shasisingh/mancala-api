package com.shashi.bol.mancala.game.v1.factory;

import com.shashi.bol.mancala.game.v1.domain.Player;
import com.shashi.bol.mancala.game.v1.repository.PlayerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class PlayerFactoryTest {

    @InjectMocks
    PlayerFactory factory;

    @Mock
    PlayerRepository repository;

    @Test
     void create() {
        when(repository.save(any()))
                .thenAnswer( invocationOnMock -> invocationOnMock.getArgument(0));
        Player player = factory.create("player1");
        assertNotNull(player);
        assertThat(player.getName(),is("player1"));
        assertThat(player.isTurn(),is(false));
        assertThat(player.isPrevTurn(),is(false));
        verify(repository).save(player);
    }

}
