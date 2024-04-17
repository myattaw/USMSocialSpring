package me.yattaw.usmsocial.user.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response class containing the count of users that the current user is following.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowingCountResponse {

    /**
     * The number of users that the current user is following.
     */
    private int followingCount;
}