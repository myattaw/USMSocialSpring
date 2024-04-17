package me.yattaw.usmsocial.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a response containing the count of posts by a user.
 *
 * <p>This class encapsulates the information required to represent the count of posts by a user in a response.
 * It includes the count of posts.</p>
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
public class PostUserCountResponse {

    /**
     * The count of posts by a user.
     */
    private Integer count;
}