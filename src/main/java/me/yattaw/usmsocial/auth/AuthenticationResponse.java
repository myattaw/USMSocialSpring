package me.yattaw.usmsocial.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO (Data Transfer Object) representing an authentication response.
 *
 * <p>
 * This class encapsulates the authentication token generated upon successful authentication.
 * </p>
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    /**
     * The authentication token generated upon successful authentication.
     */
    private String token;

}