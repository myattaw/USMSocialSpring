package me.yattaw.usmsocial.user.responses;

import org.springframework.security.core.AuthenticationException;

public class AuthenicationException extends AuthenticationException {

    public AuthenicationException(String msg) {
        super(msg);
    }
}
