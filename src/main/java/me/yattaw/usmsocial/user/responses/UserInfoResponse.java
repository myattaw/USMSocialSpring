package me.yattaw.usmsocial.user.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.*;

/**
 * Response class containing user information along with flags indicating whether the current user is following this user
 * and whether it's the current user's own profile.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    /**
     * The user information.
     */
    private UserInfo user;

    /**
     * Flag indicating whether the current user is following this user.
     */
    private boolean isFollowing;

    /**
     * Flag indicating whether it's the current user's own profile.
     */
    private boolean isOwnProfile;
}
