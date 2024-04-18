package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * Represents the composite key for the relationship between a follower
 * and the user being followed.
 * This class is used as the composite key for the UserFollower entity.
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowerId implements Serializable {

    /**
     * follower - the user who is following.
     */
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    /**
     * following - the user who is being followed.
     */
    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

}