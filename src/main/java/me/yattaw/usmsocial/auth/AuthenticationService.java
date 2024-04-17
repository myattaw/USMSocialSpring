package me.yattaw.usmsocial.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.EmailSenderService;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.AuthenicationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
/**
 * Service responsible for handling authentication-related operations.
 *
 * <p>
 * This service manages user registration, authentication, and authorization processes.
 * </p>
 *
 * @version 17 April 2024
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService senderService;

    /**
     * Registers a user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return An authentication response containing the generated JWT token.
     */
    public AuthenticationResponse register(RegisterRequest request) {

        if (request.isOAuthRegistration()) {
            // register with oAuth
            User user = handleOAuthRegistration(request);
            // Continue with the registration process...
            // (generate verification token, save to the database, generate JWT, send email, etc.)
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {

            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.GUEST)
                    .timestamp(LocalDateTime.now())
                    .build();

            user.generateVerificationToken();

            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);

            senderService.sendEmail(
                    user,
                    "Verify Email Address for USM Social",
                    "Thank you for signing up for an account on USM Social! " +
                            "Before we begin, we want to ensure that it's really you. " +
                            "Please click the button below to verify your email address:",

                    "https://www.mainecollege.tech/#/verify/" + user.getVerificationToken(),
                    "Verify Account"
            );


            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }

    /**
     * Handles the registration process for OAuth users.
     *
     * @param request The registration request containing OAuth user details.
     * @return The user entity representing the registered OAuth user.
     */
    private User handleOAuthRegistration(RegisterRequest request) {
        // Extract user information from the OAuth response
        String email = request.getEmail(); // or retrieve from OAuth response
        String firstName = request.getFirstName(); // or retrieve from OAuth response
        String lastName = request.getLastName(); // or retrieve from OAuth response

        // Check if the user already exists in the database
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            // Update user information if necessary
            User user = existingUser.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            // Update other user fields as needed
            return user;
        } else {
            // Create a new user
            return User.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .role(Role.GUEST) // Set default role for OAuth users
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param request The authentication request containing user credentials.
     * @return An authentication response containing the generated JWT token.
     * @throws AuthenticationException If authentication fails.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        if (user.getVerificationToken() != null && user.isVerified()) {
            // User remembered their password so delete their verification token
            user.setVerificationToken(null);
            userRepository.save(user);
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Retrieves the currently authenticated user based on the provided HTTP servlet request.
     *
     * @param servletRequest The HTTP servlet request containing the user's authentication token.
     * @return An optional containing the currently authenticated user, or empty if not authenticated.
     */
    public Optional<User> getCurrentUser(
            HttpServletRequest servletRequest
    ) {
        String token = jwtService.extractToken(servletRequest);
        return userRepository.findByEmail(jwtService.fetchEmail(token));
    }

    /**
     * Checks if the user associated with the provided HTTP servlet request is authorized to access the resource.
     *
     * @param servletRequest The HTTP servlet request containing the user's authentication token.
     * @throws AuthenticationException If the user is not authorized to access the resource.
     */
    public void isAuthorizedAccess(
            HttpServletRequest servletRequest
    ) throws AuthenticationException {
        Optional<User> userRequesting = getCurrentUser(servletRequest);

        if (userRequesting.isEmpty() || userRequesting.get().getRole() == Role.GUEST) {
            throw new AuthenicationException("Only users can access");
        }
    }

}
