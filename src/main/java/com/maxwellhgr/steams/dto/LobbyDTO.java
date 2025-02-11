package com.maxwellhgr.steams.dto;

import com.maxwellhgr.steams.entities.User;
import java.util.Set;

public record LobbyDTO(String game, Set<User> users, User owner) {
}
