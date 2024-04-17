package me.yattaw.usmsocial.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * A DTO (Data Transfer Object) representing an authentication request.
 *
 * <p>
 * This class encapsulates the email and password used for user authentication.
 * </p>
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    /**
     * The email of the user for authentication.
     */
    private String email;

    /**
     * The password of the user for authentication.
     */
    private String password;

}