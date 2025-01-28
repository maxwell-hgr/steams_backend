package com.maxwellhgr.steams.services;

import com.maxwellhgr.steams.dto.UserDTO;
import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SteamService steamService;

    @Autowired
    public UserService(UserRepository userRepository, SteamService steamService) {
        this.userRepository = userRepository;
        this.steamService = steamService;
    }

    public UserDTO find(String id) {
        User user = userRepository.findById(id).orElse(null);
        return user == null ? null : userToUserDTO(user);
    }


    public void create(String id) {
        User newUser = steamService.steamUser(id);
    }


    private UserDTO userToUserDTO(User user){
        return new UserDTO(user.getUsername(), user.getAvatar());
    }

    public String steamId(String claimedId) {
        if (claimedId != null && claimedId.contains("https://steamcommunity.com/openid/id/")) {
            return claimedId.substring(claimedId.lastIndexOf("/") + 1);
        }
        return null;
    }
}
