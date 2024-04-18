package me.yattaw.usmsocial.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
/**
 * Controller class handling authentication-related endpoints.
 *
 * <p>
 * This controller provides endpoints for user registration and authentication.
 * </p>
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * Endpoint for handling Google OAuth2 login callback.
     *
     * @param oauth2User The OAuth2User object containing user information from Google OAuth2.
     * @return A ResponseEntity containing an {@link AuthenticationResponse}.
     */
    @GetMapping("/register/oauth2/")
    public ResponseEntity<AuthenticationResponse> googleLoginCallback(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");

        // Use the information to create a RegisterRequest
        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .isOAuthRegistration(true)
                .build();

        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Endpoint for user registration.
     *
     * @param request The {@link RegisterRequest} containing user registration information.
     * @return A ResponseEntity containing an {@link AuthenticationResponse}.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Endpoint for user authentication.
     *
     * @param request The {@link AuthenticationRequest} containing user authentication credentials.
     * @return A ResponseEntity containing an {@link AuthenticationResponse}.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}