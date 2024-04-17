package me.yattaw.usmsocial.user.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Response class for user actions, indicating the status of the action and an optional message.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActionResponse {

    /**
     * The status code of the action.
     */
    private int status;

    /**
     * A message providing additional information about the action.
     */
    private String message;
}
