package com.maxwellhgr.steams.controllers;

import com.maxwellhgr.steams.dto.GameDTO;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.infra.security.TokenService;
import com.maxwellhgr.steams.services.SteamService;
import com.maxwellhgr.steams.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final SteamService steamService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService, SteamService steamService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.steamService = steamService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.profile(request));
    }

    @GetMapping("/games")
    public ResponseEntity<Set<GameDTO>> getUserGames(HttpServletRequest request) {
        String id = tokenService.recoverIdFromRequest(request);
        Set<GameDTO> games = steamService.getOwnedGames(id);
        return ResponseEntity.ok().body(games);
    }

    @GetMapping("/friends")
    public ResponseEntity<Set<User>> getUserFriends(HttpServletRequest request) {
        String id = tokenService.recoverIdFromRequest(request);
        Set<User> friends = steamService.getFriendsFromId(id);
        return ResponseEntity.ok().body(friends);
    }
}
