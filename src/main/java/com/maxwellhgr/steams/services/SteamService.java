package com.maxwellhgr.steams.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.maxwellhgr.steams.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class SteamService {

    @Value("${steam.api.key}")
    private String key;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public SteamService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public User steamUser(String id) {
        String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + key + "&steamids=" + id;
        String steamData = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

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
}
