package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Represents a response containing information about a post like.
 *
 * <p>This class encapsulates the information required to represent a post like in a response.
 * It includes the like ID, the first name and last name of the user who liked the post, and the timestamp of the like.</p>
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
public class PostLikeResponse {

    /**
     * The ID of the post like.
     */
    private Long likeID;

    /**
     * The first name of the user who liked the post.
     */
    private String likerFirstName;

    /**
     * The last name of the user who liked the post.
     */
    private String likerLastName;

    /**
     * The timestamp of the like.
     */
    private LocalDateTime timestamp;
}