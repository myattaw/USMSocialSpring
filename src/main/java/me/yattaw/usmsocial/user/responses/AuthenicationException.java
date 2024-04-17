package me.yattaw.usmsocial.user.responses;

import org.springframework.security.core.AuthenticationException;
/**
 * Custom exception class to handle authentication errors.
 *
 * @version 17 April 2024
 */
public class AuthenicationException extends AuthenticationException {

    /**
     * Constructs a new AuthenticationException with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AuthenicationException(String msg) {
        super(msg);
    }
}
