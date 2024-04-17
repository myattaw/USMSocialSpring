package me.yattaw.usmsocial.messages.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Represents information about a recent message.
 *
 * <p>This class encapsulates details about a recent message, including the user ID of the sender,
 * the first name and last name of the sender, the sender's tagline, base64-encoded image,
 * the content of the last message sent, the full name of the last sender,
 * the ID of the last sender, and the timestamp of the last message.</p>
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
public class RecentMessageInfo {

    /**
     * The ID of the user who sent the last message.
     */
    private Integer userId;

    /**
     * The first name of the user who sent the last message.
     */
    private String firstName;

    /**
     * The last name of the user who sent the last message.
     */
    private String lastName;

    /**
     * The tagline of the user who sent the last message.
     */
    private String tagLine;

    /**
     * The base64-encoded image of the user who sent the last message.
     */
    private String base64Image;

    /**
     * The content of the last message sent.
     */
    private String lastMessage;

    /**
     * The full name of the last sender.
     */
    private String lastSenderFullName;

    /**
     * The ID of the last sender.
     */
    private Integer lastSenderId;

    /**
     * The timestamp when the last message was sent.
     */
    private LocalDateTime timestamp;
}