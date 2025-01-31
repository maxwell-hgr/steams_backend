package com.maxwellhgr.steams.controllers;

import com.maxwellhgr.steams.dto.ResponseDTO;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.infra.security.TokenService;
import com.maxwellhgr.steams.services.SteamService;
import com.maxwellhgr.steams.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {


    @Value("${application.url}")
    private String applicationUrl;

    private final UserService userService;
    private final TokenService tokenService;
    private final SteamService steamService;

    @Autowired
    public AuthController(UserService userService, TokenService tokenService, SteamService steamService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.steamService = steamService;
    }

    @GetMapping
    public ResponseEntity<Void> steamLogin(HttpServletRequest request) {
        String openidLoginEndpoint = "https://steamcommunity.com/openid/login";
        String returnToUrl = applicationUrl + "/login/callback";

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
    public ResponseEntity<ResponseDTO> handleSteamCallback(@RequestParam Map<String, String> params) {
        String openidMode = params.get("openid.mode");
        if ("id_res".equals(openidMode)) {
            String steamId = userService.steamId(params.get("openid.claimed_id"));

            boolean isValid = verifySteamResponse(params);
            if (!isValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (steamId != null) {
                steamService.steamUser(steamId);

                String token = this.tokenService.generateToken(steamId);
                return ResponseEntity.ok().body(new ResponseDTO(steamId, token));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    private boolean verifySteamResponse(Map<String, String> params) {
        String steamOpenIdUrl = "https://steamcommunity.com/openid/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        params.forEach(body::add);
        body.add("openid.mode", "check_authentication");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(steamOpenIdUrl, requestEntity, String.class);

        return response.getBody() != null && response.getBody().contains("is_valid:true");
    }

}
