package com.maxwellhgr.steams.repositories;

import com.maxwellhgr.steams.entities.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, String> {
}
