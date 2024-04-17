package me.yattaw.usmsocial.user.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A data class representing a request to upload a user profile image.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpload {

    /** The base64-encoded image data. */
    private String imageBase64;
}
