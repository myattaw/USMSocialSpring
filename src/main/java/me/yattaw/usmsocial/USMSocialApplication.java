package me.yattaw.usmsocial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class USMSocialApplication {

    public static void main(String[] args) {
        SpringApplication.run(USMSocialApplication.class, args);
    }

    @Bean
    public CommandLineRunner createDefaults(
            JdbcTemplate jdbcTemplate,
            BCryptPasswordEncoder encoder,
            UserRepository userRepository
    ) {
        return (args) -> {
            int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usm_social_users", Integer.class);
            if (rowCount == 0) {// always make sure there is at least 1 user added to the database or create an admin account.
                log.info("Creating new admin account...");
                // Create an admin user on first start up
                String adminPassword = generateRandomString(32);
                userRepository.save(
                        User.builder()
                                .firstName("USMSocial")
                                .lastName("Admin")
                                .email("admin")
                                .password(encoder.encode(adminPassword))
                                .role(Role.ADMIN)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
                log.info("Created new admin account with password:" + adminPassword);
            }
        };
    }

    public static String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        // Encode the random bytes to a Base64 string
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, length);
    }

}
