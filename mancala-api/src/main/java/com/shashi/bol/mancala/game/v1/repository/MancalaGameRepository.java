
package com.shashi.bol.mancala.game.v1.repository;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MancalaGameRepository extends JpaRepository<MancalaGame, Long> {
    Optional<MancalaGame> findByGameId(String gameId);
}
