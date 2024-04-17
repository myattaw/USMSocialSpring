package me.yattaw.usmsocial.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Request object used for admin deletion operations.
 *
 * <p>
 * This class encapsulates the information required for admin deletion operations, such as deleting a user or a post.
 * </p>
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDeleteRequest {

    /**
     * The ID of the target entity to delete.
     */
    private int targetId;

}
