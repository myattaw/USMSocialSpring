package me.yattaw.usmsocial.auth;

import java.util.Optional;

import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.AuthenicationException;

public class AuthenticationHelper {
    static public Optional<User> getCurrentUser(HttpServletRequest servletRequest, 
            JwtService jwtService, UserRepository userRepository) {
        String token = jwtService.extractToken(servletRequest);
        return userRepository.findByEmail(jwtService.fetchEmail(token));
    }

    static public void isAuthorizedAccess(HttpServletRequest servletRequest,
            JwtService jwtService, UserRepository userRepository) throws AuthenticationException {
        Optional<User> userRequesting = getCurrentUser(servletRequest, jwtService, userRepository);

        if (userRequesting.isEmpty() || userRequesting.get().getRole() == Role.GUEST) {
            throw new AuthenicationException("Only users can access");
        }
    }
}