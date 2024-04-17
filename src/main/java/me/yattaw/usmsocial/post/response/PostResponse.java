package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
/**
 * Represents a response containing paginated post information.
 *
 * <p>This class encapsulates the information required to represent paginated post data in a response.
 * It includes a page result containing formatted posts and the date and time of the fetch.</p>
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
public class PostResponse {

    /**
     * The page result containing formatted posts.
     */
    private Page<PostFormatResponse> pageResult;

    /**
     * The date and time of the fetch.
     */
    private LocalDateTime dateTimeFetch;
}
