package de.hskl.cloudnative.security.config;

import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.hskl.cloudnative.security.AuthUser;
import de.hskl.cloudnative.security.UserRepository;

@Configuration
public class TestDataConfig {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("InitUsers method is being called!");

            AuthUser adminUser = AuthUser.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("pass"))
                    .roles(new HashSet<>(List.of("ROLE_ADMIN")))
                    .build();

            AuthUser writerUser = AuthUser.builder()
                    .firstName("Writer")
                    .lastName("User")
                    .email("writer@example.com")
                    .password(passwordEncoder.encode("pass"))
                    .roles(new HashSet<>(List.of("ROLE_PLAYER")))
                    .build();

            AuthUser defaultUser = AuthUser.builder()
                    .firstName("Default")
                    .lastName("User")
                    .email("default@example.com")
                    .password(passwordEncoder.encode("pass"))
                    .roles(new HashSet<>(List.of("ROLE_USER")))
                    .build();
                    
            if(!userRepository.findByEmail(adminUser.getEmail()).isPresent()) userRepository.save(adminUser);
            if(!userRepository.findByEmail(writerUser.getEmail()).isPresent()) userRepository.save(writerUser);
            if(!userRepository.findByEmail(defaultUser.getEmail()).isPresent()) userRepository.save(defaultUser);

        };
    }
}
