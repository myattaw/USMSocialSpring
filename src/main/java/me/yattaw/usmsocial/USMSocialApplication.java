package me.yattaw.usmsocial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Main class for the USMSocial application.
 *
 * <p>
 * This class initializes and runs the Spring Boot application for USMSocial.
 * It also contains a method to create default admin user if no users exist in the database.
 * </p>
 *
 * @version 17 April 2024
 */
@Slf4j
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@RequiredArgsConstructor
public class USMSocialApplication {

    /**
     * Entry point of the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(USMSocialApplication.class, args);
    }

    /**
     * Creates default admin user if no users exist in the database.
     *
     * @param jdbcTemplate   The JdbcTemplate for executing SQL queries.
     * @param encoder        The BCryptPasswordEncoder for encoding passwords.
     * @param userRepository The repository for accessing user data.
     * @return A CommandLineRunner that creates the default admin user.
     */
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
                                .password(encoder.encode("admin")) // user adminPassword later
                                .role(Role.ADMIN)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
                log.info("Created new admin account with password:" + adminPassword);
            }
        };
    }

    /**
     * Generates a random string of specified length.
     *
     * @param length The length of the random string to generate.
     * @return The randomly generated string.
     */
    public static String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        // Encode the random bytes to a Base64 string
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, length);
    }

}
