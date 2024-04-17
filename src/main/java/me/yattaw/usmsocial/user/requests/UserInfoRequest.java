package me.yattaw.usmsocial.user.requests;

import me.yattaw.usmsocial.entities.user.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * A data class representing a request to update user information.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {

    /** The ID of the user. */
    private int id;

    /** The first name of the user. */
    private String firstName;

    /** The last name of the user. */
    private String lastName;

    /** The email of the user. */
    private String email;

    /** The tagline of the user. */
    private String tagLine;

    /** The biography of the user. */
    private String bio;
}