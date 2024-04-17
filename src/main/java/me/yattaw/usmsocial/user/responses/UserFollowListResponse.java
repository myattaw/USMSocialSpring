package me.yattaw.usmsocial.user.responses;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response class containing a page of users that the current user is following.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowListResponse {

    /**
     * The page of users that the current user is following.
     */
    private Page<UserSearch> userFollowList;
}
