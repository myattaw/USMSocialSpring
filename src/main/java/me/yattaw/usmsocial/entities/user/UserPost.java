package me.yattaw.usmsocial.entities.user;

import jakarta.persistence.*;
import lombok.*;
import me.yattaw.usmsocial.entities.post.PostComment;
import me.yattaw.usmsocial.entities.post.PostLike;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a post made by a user in the social network system.
 *
 * This class stores information about user posts, including the post content, the user who made the post,
 * likes received by the post, comments made on the post, and the timestamp of the post.
 *
 * @version 17 April 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "likes")
@Table(name = "usm_social_posts")
public class UserPost {

    /**
     * The unique identifier for this post.
     */
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The content of this post.
     */
    @Column(length = 280)
    private String content;

    /**
     * The user who made this post.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The set of likes received by this post.
     */
    @OneToMany(mappedBy = "post")
    @OrderBy("timestamp ASC")
    private Set<PostLike> likes = new HashSet<>();

    /**
     * The list of comments made on this post.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    /**
     * The timestamp indicating when this post was created.
     */
    private LocalDateTime timestamp;

    /**
     * The transient field for dynamic calculation of the total number of likes
     * on this post.
     */
    @Transient
    private int likeCount;

    /**
     * Updates the total number of likes for this post.
     */
    public void updateLikeCount() {
        this.likeCount = likes.size();
    }

    /**
     * Adds a like to this post.
     *
     * @param like The like to be added.
     */
    public void addLike(PostLike like) {
        likes.add(like);
        updateLikeCount();
    }

    /**
     * Removes a like from this post.
     *
     * @param like The like to be removed.
     */
    public void removeLike(PostLike like) {
        likes.remove(like);
        updateLikeCount();
    }

}
