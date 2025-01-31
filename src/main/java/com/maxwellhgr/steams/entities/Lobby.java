package com.maxwellhgr.steams.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_lobbies")
public class Lobby implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @ManyToMany(mappedBy = "lobbies")
    private final Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
    }
}
