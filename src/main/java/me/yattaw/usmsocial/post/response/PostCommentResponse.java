package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Represents a response containing information about a post comment.
 *
 * <p>This class encapsulates the information required to represent a post comment in a response.
 * It includes the comment ID, content, commenter's first name, last name, and timestamp.</p>
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
public class PostCommentResponse {

    /**
     * The ID of the comment.
     */
    private Long commentId;

    /**
     * The content of the comment.
     */
    private String content;

    /**
     * The first name of the commenter.
     */
    private String commenterFirstName;

    /**
     * The last name of the commenter.
     */
    private String commenterLastName;

    /**
     * The timestamp of the comment.
     */
    private LocalDateTime timestamp;

    /**
     * Base 64 Encoding of profile picture
     */
    private String profilePictureBase64;
}
