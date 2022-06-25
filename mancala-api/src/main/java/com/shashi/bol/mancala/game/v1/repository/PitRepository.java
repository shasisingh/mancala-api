
package com.shashi.bol.mancala.game.v1.repository;

import com.shashi.bol.mancala.game.v1.domain.MancalaPit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PitRepository extends JpaRepository<MancalaPit, Long> {
}
