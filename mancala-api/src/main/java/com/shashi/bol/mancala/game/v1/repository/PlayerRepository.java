
package com.shashi.bol.mancala.game.v1.repository;

import com.shashi.bol.mancala.game.v1.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
