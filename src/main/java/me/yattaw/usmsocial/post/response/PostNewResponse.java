package me.yattaw.usmsocial.post.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a response containing information about new posts.
 *
 * <p>This class encapsulates the information required to represent new posts in a response.
 * It includes a list of post format responses representing the new posts and the server date and time.</p>
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
public class PostNewResponse {

    /**
     * The list of post format responses representing the new posts.
     */
    private List<PostFormatResponse> posts;

    /**
     * The server date and time.
     */
    private LocalDateTime serverDateTime;
}