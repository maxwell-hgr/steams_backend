package com.maxwellhgr.steams.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxwellhgr.steams.entities.Game;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.repositories.GameRespository;
import com.maxwellhgr.steams.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class SteamService {

    @Value("${steam.api.key}")
    private String key;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final GameRespository gameRepository;

    @Autowired
    public SteamService(RestTemplate restTemplate, UserRepository userRepository, GameRespository gameRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public void saveSteamData(String id) {
        User user = steamUser(id);
        Set<Game> games = getOwnedGames(id);
        Set<User> friends = getFriendsFromId(id);

        user.setGames(games);
        user.setFriends(friends);
        userRepository.save(user);
    }

    public User steamUser(String id) {
        String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + key + "&steamids=" + id;
        String steamData = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(steamData);
            JsonNode playerNode = rootNode.path("response").path("players").get(0);

            User user = new User();
            user.setId(playerNode.path("steamid").asText());
            user.setUsername(playerNode.path("personaname").asText());
            user.setAvatar(playerNode.path("avatarfull").asText());

            return user;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<User> getFriendsFromId(String id) {
        System.out.println(3);
        String url = "https://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" + key + "&steamid=" + id + "&relationship=friend";
        String friendsData = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Set<User> friends = new HashSet<>();
        try {
            JsonNode rootNode = mapper.readTree(friendsData);
            JsonNode friendsNode = rootNode.path("friendslist").path("friends");

            for (JsonNode friend : friendsNode) {
                String steamId = friend.path("steamid").asText();

                User user = userRepository.findById(steamId).orElse(null);

                if(user == null) {
                    user = new User();
                    user.setId(steamId);
                    userRepository.save(user);
                }

                friends.add(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return friends;
    }

    public Set<Game> getOwnedGames(String id) {
        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + key + "&steamid=" + id + "&include_appinfo=1&include_played_free_games=1";
        String gamesData = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Set<Game> gamesList = new HashSet<>();
        try {
            JsonNode rootNode = mapper.readTree(gamesData);
            JsonNode gamesNode = rootNode.path("response").path("games");

            for (JsonNode gameNode : gamesNode) {
                Game game = new Game();
                String appId = gameNode.path("appid").asText();
                game.setAppId(appId);


                // this cant happen
                gameRepository.save(game);

                gamesList.add(game);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return gamesList;
    }
}
