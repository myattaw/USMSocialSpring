package me.yattaw.usmsocial.post.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a request for paginated user posts.
 *
 * <p>This class encapsulates the information required to request a specific page of user posts.
 * It includes the page number and page size.</p>
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
public class UserPostPageRequest {

    /**
     * The page number of the requested page.
     */
    private int pageNumber;

    /**
     * The size of the page requested.
     */
    private int pageSize;
}
