package me.yattaw.usmsocial;

import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;

@SpringBootApplication
public class USMSocialApplication {

    public USMSocialApplication(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public static void main(String[] args) {
		SpringApplication.run(USMSocialApplication.class, args);
	}

	private final JwtService jwtService;

	@Bean
	public CommandLineRunner createDefaults(
			JdbcTemplate jdbcTemplate,
			BCryptPasswordEncoder encoder,
			JwtService jwtService,
			UserRepository userRepository
	) {
		return (args) -> {
			int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usm_social_users", Integer.class);
			if (rowCount == 0) {// always make sure there is at least 1 user added to the database or create an admin account.
				System.out.println("Creating new admin account...");
				// Create an admin user on first start up
				UserDetails userDetails = userRepository.save(
						User.builder()
								.firstName("admin")
								.lastName("admin")
								.email("admin")
								.password(encoder.encode("admin"))
								.role(Role.ADMIN)
								.timestamp(LocalDateTime.now())
								.build()
				);
				String token = jwtService.generateToken(new HashMap<>(), userDetails);
				System.out.println("admin token: " + token);
				System.out.println("jwt decode: " + jwtService.fetchClaims(token));
			}
		};
	}


	// Bearer token is created for a username/email which should be used when sending
	// requests to the REST API.
	//
	// We can fetch the username from the token to determine who is performing the actions
	// on the REST API.
	//TODO: REMOVE THIS ONCE DONE TESTING
	@EventListener
	public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		// Printing out tokens on authentication to test REST API
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			System.out.println("User logged in: " + userDetails.getUsername());
			System.out.println("token: " + jwtService.generateToken(new HashMap<>(), userDetails));
		}
	}

}
