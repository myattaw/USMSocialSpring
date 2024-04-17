package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.user.PostUserInfo;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Represents a response containing formatted post information.
 *
 * <p>This class encapsulates the information required to represent a post in a formatted response.
 * It includes the post ID, content, information about the user who posted it, comments on the post,
 * timestamp of the post, like count, and whether the post is liked by the current user.</p>
 *
 * {@code @Data} is a Lombok annotation to generate getters, setters, equals, hashcode, and toString methods.
 * {@code @Builder} is a Lombok annotation to generate a builder for the class.
 * {@code @AllArgsConstructor} generates a constructor with all arguments.
 * {@code @NoArgsConstructor} generates a constructor with no arguments.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFormatResponse {

    /**
     * The ID of the post.
     */
    private Integer id;

    /**
     * The content of the post.
     */
    private String content;

    /**
     * Information about the user who posted the post.
     */
    private PostUserInfo postUserInfo;

    /**
     * Comments on the post.
     */
    private List<PostCommentResponse> comments;

    /**
     * The timestamp of the post.
     */
    private LocalDateTime timestamp;

    /**
     * The count of likes on the post.
     */
    private int likeCount;

    /**
     * A boolean indicating whether the post is liked by the current user.
     */
    private boolean isLiked;
}