package com.maxwellhgr.steams.services;

import com.maxwellhgr.steams.dto.UserDTO;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.infra.security.SecurityFilter;
import com.maxwellhgr.steams.infra.security.TokenService;
import com.maxwellhgr.steams.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public User profile(HttpServletRequest request) {
        String id = tokenService.recoverIdFromRequest(request);
        return find(id);
    }

    public User find(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public String steamId(String claimedId) {
        if (claimedId != null && claimedId.contains("https://steamcommunity.com/openid/id/")) {
            return claimedId.substring(claimedId.lastIndexOf("/") + 1);
        }
        return null;
    }
}
