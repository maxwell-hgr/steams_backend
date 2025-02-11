package com.maxwellhgr.steams.services;

import com.maxwellhgr.steams.dto.LobbyDTO;
import com.maxwellhgr.steams.entities.Lobby;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.repositories.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UserService userService;

    @Autowired
    public LobbyService(LobbyRepository lobbyRepository, UserService userService) {
        this.lobbyRepository = lobbyRepository;
        this.userService = userService;
    }

    public Lobby save(LobbyDTO lobbyDTO) {
        Lobby lobby = new Lobby();
        lobby.setUsers(lobbyDTO.users());
        lobby.setOwner(lobbyDTO.owner());
        lobby.setGame(lobbyDTO.game());

        lobbyRepository.save(lobby);
        return lobby;
    }

    public void delete(String id) {
        lobbyRepository.deleteById(id);
    }

    public LobbyDTO get(String id) {
        Lobby lobby = lobbyRepository.findById(id).orElse(null);
        if (lobby != null) {
            return new LobbyDTO(lobby.getGame(), lobby.getUsers(), lobby.getOwner());
        }
        return null;
    }

    public List<Lobby> getAll() {
        return lobbyRepository.findAll();
    }

    public Set<Lobby> getAllByOwner(String owner) {
        User user = userService.find(owner);
        return user.getLobbies();
    }
}
