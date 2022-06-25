package com.shashi.bol.mancala.game.v1.mapper;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.model.GameDTO;
import com.shashi.bol.mancala.game.v1.utils.GameUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class DTOMapperTest {

    @Test
    void mapToDto() {
        DTOMapper mapper= new DTOMapper();
        GameDTO mapped = mapper.mapToDto(GameUtils.getNewGame(UUID.randomUUID().toString()));
        assertNotNull(mapped);
        assertThat(mapped.getCurrentPitIndex()).isZero();
        assertThat(mapped.getGameId()).isNotNull();
        assertThat(mapped.getPlayer1().getName()).isEqualTo("Wolverine");
        assertThat(mapped.getPlayer2().getName()).isEqualTo("Deadpool");
        assertThat(mapped.getGameStatus()).isEqualTo(MancalaGame.GameStatus.INITIATED);
        assertThat(mapped.getMancalaPits().size()).isEqualTo(14);
    }
}
