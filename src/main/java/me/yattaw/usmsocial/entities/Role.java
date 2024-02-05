package me.yattaw.usmsocial.entities;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public enum Role {

    STUDENT("ROLE_STUDENT"),
    ALUMNI("ROLE_ALUMNI"),
    STAFF("ROLE_STAFF"),
    FACULTY("ROLE_FACULTY"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public SimpleGrantedAuthority getSimpleGrantedAuthority() {
        return new SimpleGrantedAuthority(authority);
    }

}
