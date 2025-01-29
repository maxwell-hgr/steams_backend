package com.maxwellhgr.steams.controllers;

import com.maxwellhgr.steams.dto.UserDTO;
import com.maxwellhgr.steams.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.profile(request));
    }
}
