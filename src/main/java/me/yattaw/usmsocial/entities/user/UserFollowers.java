package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_followers")
public class UserFollowers {

    @EmbeddedId
    private UserFollowerId id;

    private LocalDateTime timestamp;

    @PrePersist
    @PreUpdate
    private void validateUsers() {
        if (id != null && id.getFollower() != null && id.getFollower().equals(id.getFollowing())) {
            throw new IllegalStateException("Follower and following users cannot be the same.");
        }
    }

}