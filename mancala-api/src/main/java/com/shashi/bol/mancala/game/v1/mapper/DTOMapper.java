package com.shashi.bol.mancala.game.v1.mapper;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.model.GameDTO;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public GameDTO mapToDto(final MancalaGame newGameBoard) {
        return new GameDTO(newGameBoard);
    }
}
