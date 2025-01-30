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
        User user = new User();
        user.setId("76561198150772575");
        user.setUsername("Dizas");
        user.setAvatar("https://avatars.steamstatic.com/e94f8fb93ddd23304536adbf297e1de31f5424b4_full.jpg");

        User user2 = new User();
        user2.setId("76561199522268022");
        user2.setUsername("Iury");
        user2.setAvatar("https://avatars.steamstatic.com/141475c8a9159d3386d0ce94c5bba083a631e983_full.jpg");

        User user3 = new User();
        user3.setId("76561198181013638");
        user3.setUsername("Gordão");
        user3.setAvatar("https://avatars.steamstatic.com/30cf925af21ae784472a54eaf2e8d48e9d41df1c_full.jpg");


        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

    }
}
