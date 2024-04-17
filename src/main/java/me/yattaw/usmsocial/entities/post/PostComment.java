package me.yattaw.usmsocial.entities.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;

import java.time.LocalDateTime;

/**
 * Defines the properties of a <code>PostComment</code>.
 * <code>PostComments</code> are stored in the table <code>usm_social_post_comments</code>.
 * @version 17 April 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_post_comments")
public class PostComment {

    /**
     * commentId - unique identifier for this comment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    /**
     * user - the user making this comment.
     * cannot be nullified.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * post - the post this comment is added to.
     * cannot be nullified.
     */
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private UserPost post;

    /**
     * content - the contents of this comment.
     * max length is 140 chars.
     */
    @Column(length = 140)
    private String content;

    /**
     * timestamp - the time this comment was left.
     */
    private LocalDateTime timestamp;

}
