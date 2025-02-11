package com.maxwellhgr.steams.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_lobbies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String game;

    @ManyToOne
    private User owner;

    @ManyToMany(mappedBy = "lobbies")
    private Set<User> users;

    public void addUser(User user) {
        users.add(user);
    }
}
