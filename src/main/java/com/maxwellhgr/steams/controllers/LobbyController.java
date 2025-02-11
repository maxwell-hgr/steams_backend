package com.maxwellhgr.steams.controllers;

import com.maxwellhgr.steams.dto.LobbyDTO;
import com.maxwellhgr.steams.entities.Lobby;
import com.maxwellhgr.steams.services.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping
    public ResponseEntity<List<Lobby>> getLobby() {
        return ResponseEntity.ok().body(lobbyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LobbyDTO> getLobbyById(@PathVariable String id) {
        LobbyDTO lobbyDTO = lobbyService.get(id);
        return ResponseEntity.ok().body(lobbyDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Set<Lobby>> getLobbyByUserId(@PathVariable String id) {
        return ResponseEntity.ok().body(lobbyService.getAllByOwner(id));
    }


    @PostMapping
    public ResponseEntity<String> createLobby(@RequestBody LobbyDTO lobbyDTO) {
        lobbyService.save(lobbyDTO);
        return ResponseEntity.ok().body("Lobby created");
    }

    @PutMapping
    public ResponseEntity<String> updateLobby(@RequestBody LobbyDTO lobbyDTO) {
        lobbyService.save(lobbyDTO);
        return ResponseEntity.ok().body("Lobby updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLobby(@PathVariable String id) {
        lobbyService.delete(id);
        return ResponseEntity.ok().body("Lobby deleted");
    }
}
