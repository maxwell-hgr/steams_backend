package com.maxwellhgr.steams.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "tb_lobbies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby implements Serializable {

    @Id
    private Long id;

}
