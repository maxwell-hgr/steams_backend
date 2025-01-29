package com.maxwellhgr.steams.services;

import com.maxwellhgr.steams.dto.UserDTO;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.infra.security.SecurityFilter;
import com.maxwellhgr.steams.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SteamService steamService;
    private final SecurityFilter securityFilter;

    @Autowired
    public UserService(UserRepository userRepository, SteamService steamService, SecurityFilter securityFilter) {
        this.userRepository = userRepository;
        this.steamService = steamService;
        this.securityFilter = securityFilter;
    }

    public UserDTO profile(HttpServletRequest request) {
        String id = securityFilter.recoverIdFromRequest(request);
        return find(id);
    }

    public UserDTO find(String id) {
        User user = userRepository.findById(id).orElse(null);
        return user == null ? null : userToUserDTO(user);
    }

    public UserDTO create(String id) {
        User newUser = steamService.steamUser(id);
        userRepository.save(newUser);

        return userToUserDTO(newUser);
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
