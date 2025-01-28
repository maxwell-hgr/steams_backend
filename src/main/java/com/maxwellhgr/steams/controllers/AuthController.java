package com.maxwellhgr.steams.controllers;

import com.maxwellhgr.steams.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/auth/steam")
public class AuthController {


    @Value("${application.url}")
    private String applicationUrl;

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Void> steamLogin(HttpServletRequest request) {
        String openidLoginEndpoint = "https://steamcommunity.com/openid/login";
        String returnToUrl = applicationUrl + "auth/steam/callback";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(openidLoginEndpoint)
                .queryParam("openid.mode", "checkid_setup")
                .queryParam("openid.ns", "http://specs.openid.net/auth/2.0")
                .queryParam("openid.identity", "http://specs.openid.net/auth/2.0/identifier_select")
                .queryParam("openid.claimed_id", "http://specs.openid.net/auth/2.0/identifier_select")
                .queryParam("openid.return_to", returnToUrl)
                .queryParam("openid.realm", "http://localhost:8080");

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.build().toUri());
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleSteamCallback(@RequestParam Map<String, String> params) {
        String openidMode = params.get("openid.mode");
        if ("id_res".equals(openidMode)) {
            String steamId = userService.steamId(params.get("openid.claimed_id"));
            if (steamId != null) {
                if (userService.find(steamId) == null) {
                    userService.create(steamId);
                }
                return ResponseEntity.ok().body("success"); // generates JWT token - user is redirected to home page (frontend)
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
