package me.yattaw.usmsocial.user.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user profile picture containing the image data in Base64 format.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfilePicture {

    /**
     * The Base64 encoded image data.
     */
    private String imageBase64;
}
