package com.maxwellhgr.steams.config;

import com.maxwellhgr.steams.entities.User;
import com.maxwellhgr.steams.repositories.UserRepository;
import com.maxwellhgr.steams.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class Test implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public Test(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("218372813712", "Dole", "avatar.jpg");
        User user2 = new User("21983712983712", "James", "avatar.jpg");

        userRepository.save(user);
        userRepository.save(user2);

    }
}
