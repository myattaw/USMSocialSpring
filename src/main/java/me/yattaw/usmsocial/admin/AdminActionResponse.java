package me.yattaw.usmsocial.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response object for admin actions.
 *
 * <p>
 * This class encapsulates information about the status and message of an admin action response.
 * </p>
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminActionResponse {

    /**
     * The status code of the admin action.
     */
    private int status;

    /**
     * The message associated with the admin action response.
     */
    private String message;

}
