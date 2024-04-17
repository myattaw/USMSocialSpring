package me.yattaw.usmsocial.entities.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
/**
 * Represents user roles within the system.
 * @version 17 April 2024
 */
@Getter
public enum Role {

    /**
     * Guest role.
     */
    GUEST("ROLE_GUEST"),

    /**
     * Student role.
     */
    STUDENT("ROLE_STUDENT"),

    /**
     * Alumni role.
     */
    ALUMNI("ROLE_ALUMNI"),

    /**
     * Staff role.
     */
    STAFF("ROLE_STAFF"),

    /**
     * Faculty role.
     */
    FACULTY("ROLE_FACULTY"),

    /**
     * Admin role.
     */
    ADMIN("ROLE_ADMIN");

    private final String authority;

    /**
     * Constructs a new Role enum with the specified authority.
     *
     * @param authority The authority associated with the role.
     */
    Role(String authority) {
        this.authority = authority;
    }

    /**
     * Retrieves the simple granted authority for this role.
     *
     * @return The simple granted authority.
     */
    public SimpleGrantedAuthority getSimpleGrantedAuthority() {
        return new SimpleGrantedAuthority(authority);
    }

}
