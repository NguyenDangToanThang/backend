package com.shopelec.backend.configuration;

import com.shopelec.backend.model.User;
import com.shopelec.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                String date = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                User user = User.builder()
                        .name("admin")
                        .email("admin@gmail.com")
                        .dob("16/11/2002")
                        .date_created(date)
                        .gender("Male")
                        .phoneNumber("0387185045")
                        .role("ADMIN")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(user);
            }
        };
    }

}