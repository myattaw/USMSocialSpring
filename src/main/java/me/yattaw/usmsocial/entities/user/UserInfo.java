package me.yattaw.usmsocial.entities.user;

import lombok.Getter;

@Getter
/**
 * Represents detailed information about a user, including personal details,
 * biography, and profile picture.
 *
 * This class extends the {@link PostUserInfo} class to include
 * additional information about the user's biography.
 *
 * @version 17 April 2024
 */
public class UserInfo extends PostUserInfo {

    /**
     * The biography of the user.
     */
    private String bio;

    /**
     * Constructs a new {@code UserInfo} object with the specified details.
     *
     * @param id The unique identifier of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param email The email address of the user.
     * @param tagLine The tagline associated with the user.
     * @param bio The biography of the user.
     * @param imageBase64 The base64 encoded profile picture of the user.
     */
    public UserInfo(
            int id,
            String firstName,
            String lastName,
            String email,
            String tagLine,
            String bio,
            String imageBase64
    ) {
        super(id, firstName, lastName, email, tagLine, imageBase64);
        this.bio = bio;
    }
}
