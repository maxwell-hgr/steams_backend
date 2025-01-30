package com.maxwellhgr.steams.services;

import com.maxwellhgr.steams.dto.UserDTO;
import com.maxwellhgr.steams.entities.Game;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.infra.security.SecurityFilter;
import com.maxwellhgr.steams.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityFilter securityFilter;

    @Autowired
    public UserService(UserRepository userRepository, SecurityFilter securityFilter) {
        this.userRepository = userRepository;
        this.securityFilter = securityFilter;
    }

    public User profile(HttpServletRequest request) {
        String id = securityFilter.recoverIdFromRequest(request);
        return find(id);
    }

    public User find(String id) {
        return userRepository.findById(id).orElse(null);
    }

    private UserDTO userToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getAvatar());
    }

    public String steamId(String claimedId) {
        if (claimedId != null && claimedId.contains("https://steamcommunity.com/openid/id/")) {
            return claimedId.substring(claimedId.lastIndexOf("/") + 1);
        }
        return null;
    }
}
