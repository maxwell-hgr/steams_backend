package com.maxwellhgr.steams.repositories;

import com.maxwellhgr.steams.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRespository extends JpaRepository<Game, String> {
}
