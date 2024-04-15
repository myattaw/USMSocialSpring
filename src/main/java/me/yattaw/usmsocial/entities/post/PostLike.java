package me.yattaw.usmsocial.entities.post;

import jakarta.persistence.*;
import lombok.*;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;

import java.time.LocalDateTime;

/**
 * Defines the properties of a <code>PostLike</code>.
 * <code>PostLikes</code> are stored in the table <code>usm_social_post_likes</code>.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "usm_social_post_likes")
public class PostLike {

    /**
     * likeId - unique identifier for this like.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    /**
     * user - the user leaving this like.
     * cannot be nullified.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * post - the post this like is added to.
     * cannot be nullified.
     */
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private UserPost post;

    /**
     * timestamp - the time this like was added.
     */
    private LocalDateTime timestamp;

}
