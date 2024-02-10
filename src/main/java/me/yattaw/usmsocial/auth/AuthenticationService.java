package me.yattaw.usmsocial.auth;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.service.EmailSenderService;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService senderService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
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

                "http://localhost:8080/api/v1/verify/" + user.getVerificationToken(),
                "Verify Account"
        );

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
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

}
