package com.store.storemanagement.service;

import com.store.storemanagement.entity.Role;
import com.store.storemanagement.entity.User;
import com.store.storemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${user.email}")
    private String email;

    @Value("${user.username}")
    private String username;

    @Value("${user.password}")
    private String password;

    @Value("${user.role}")
    private String role;

    @Override
    public void run(String... args) {
        if (!userRepository.existsById(email)) {
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.valueOf(role));
            userRepository.save(user);

        }

    }
}
