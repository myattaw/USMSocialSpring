package me.yattaw.usmsocial.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a registration request for creating a new user account.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The password for the user account.
     */
    private String password;

    /**
     * Flag indicating whether the registration is performed via OAuth.
     * Initialized to <code>false</code>.
     */
    private boolean isOAuthRegistration = false;

}
