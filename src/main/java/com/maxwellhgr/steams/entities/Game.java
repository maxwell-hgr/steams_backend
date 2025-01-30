package com.maxwellhgr.steams.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tb_games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String appId;
    private String name;
    private String bannerUrl;

    @JsonIgnore
    @ManyToMany(mappedBy = "games")
    private Set<User> users = new HashSet<>();
}
