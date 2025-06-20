package org.example.user_service.config;

import lombok.RequiredArgsConstructor;
import org.example.user_service.entity.Role;
import org.example.user_service.entity.RoleSystem;
import org.example.user_service.entity.User;
import org.example.user_service.repository.RoleRepository;
import org.example.user_service.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (roleRepository.findByName(RoleSystem.ADMIN.name()).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(RoleSystem.ADMIN.name())
                        .description("Admin of system")
                        .build());
            }
            if (roleRepository.findByName(RoleSystem.USER.name()).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(RoleSystem.USER.name())
                        .description("User of system")
                        .build());
            }
            if (userRepository.findUserByUsername(RoleSystem.ADMIN.name().toLowerCase(Locale.ROOT)).isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .phoneNumber("0357753844")
                        .email("admin@gmail.com")
                        .status(true)
                        .role(roleRepository.findByName(RoleSystem.ADMIN.name()).get())
                        .password(passwordEncoder().encode("password"))
                        .build();
                userRepository.save(user);
            }
        };
    }
}
