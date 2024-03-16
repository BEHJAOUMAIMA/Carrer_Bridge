package com.example.carrer_bridge.seeder;

import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed users if not already present
       // if (userRepository.count() == 0) {
       //     User user = new User();
        //    user.setFirstName("Ouma");
         //   user.setLastName("BEHJA");
        //    user.setEmail("Ouma@example.com");
         //   user.setPassword(passwordEncoder.encode("password")); // Encode password

           // userRepository.save(user);
    }
}
