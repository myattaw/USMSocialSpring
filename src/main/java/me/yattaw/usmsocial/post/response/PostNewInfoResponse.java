package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Represents a response containing information about new posts.
 *
 * <p>This class encapsulates the information required to represent information about new posts in a response.
 * It includes the amount of new posts, the server date and time, and the date and time of the last fetch.</p>
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
public class PostNewInfoResponse {

    /**
     * The amount of new posts.
     */
    private Integer amountOfNewPost;

    /**
     * The server date and time.
     */
    private LocalDateTime serverDateTime;

    /**
     * The date and time of the last fetch.
     */
    private LocalDateTime lastFetchDateTime;
}