package me.yattaw.usmsocial.entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * Represents user information for a post.
 * @version 17 April 2024
 */
@Getter
@AllArgsConstructor
public class PostUserInfo {

    /**
     * The unique identifier for the user.
     */
    private int id;

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
     * The tagline associated with the user.
     */
    private String tagLine;

    /**
     * The base64 encoded image associated with the user.
     */
    private String base64Image;

}
