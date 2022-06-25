package com.shashi.bol.mancala.game.v1.factory;

import com.shashi.bol.mancala.game.v1.domain.Player;
import com.shashi.bol.mancala.game.v1.repository.PlayerRepository;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {
    private final PlayerRepository playerRepository;

    public PlayerFactory(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player create(String playerName) {
        Player player = new Player();
        player.setName(playerName);
        return save(player);
    }

    private Player save(Player player) {
        return playerRepository.save(player);
    }

}
