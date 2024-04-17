package me.yattaw.usmsocial.messages.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a response containing message details.
 *
 * <p>This class encapsulates information about a message, including the user ID of the sender,
 * the content of the message, the first name and last name of the sender, and the timestamp
 * when the message was sent.</p>
 *
 * {@code @Data} is a Lombok annotation to generate getters, setters, equals, hashCode, and toString methods.
 * {@code @Builder} is a Lombok annotation to generate a builder pattern for object creation.
 * {@code @NoArgsConstructor} is a Lombok annotation to generate a constructor with no arguments.
 * {@code @AllArgsConstructor} is a Lombok annotation to generate a constructor with all arguments.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    /**
     * The ID of the user who sent this message.
     */
    private Integer userId;

    /**
     * The content of this message.
     */
    private String content;

    /**
     * The first name of the user who sent this message.
     */
    private String firstName;

    /**
     * The last name of the user who sent this message.
     */
    private String lastName;

    /**
     * The timestamp when this message was sent.
     */
    private LocalDateTime timestamp;
}