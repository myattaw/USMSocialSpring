package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Represents the relationship between users where one user follows another.
 *
 * @version 17 April 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_followers")
public class UserFollowers {

    /**
     * The composite key representing the follower and the user being followed.
     */
    @EmbeddedId
    private UserFollowerId id;

    /**
     * The timestamp indicating when the follow action occurred.
     */
    private LocalDateTime timestamp;

    /**
     * Validates that the follower and following users are not the same.
     * Throws an IllegalStateException if they are the same.
     */
    @PrePersist
    @PreUpdate
    private void validateUsers() {
        if (id != null && id.getFollower() != null && id.getFollower().equals(id.getFollowing())) {
            throw new IllegalStateException("Follower and following users cannot be the same.");
        }
    }

}