package me.yattaw.usmsocial.post.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a request to create or update a user post.
 *
 * <p>This class encapsulates the information required to create or update a user post.
 * It includes the post ID and the content of the post.</p>
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
@AllArgsConstructor
@NoArgsConstructor
public class UserPostRequest {

    /**
     * The ID of the user post.
     */
    private int id;

    /**
     * The content of the user post.
     */
    private String content;
}
