package me.yattaw.usmsocial.messages.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a request to send a message.
 *
 * <p>This class encapsulates the content of the message to be sent.</p>
 *
 * {@code @Data} is a Lombok annotation to generate getters, setters, equals, hashCode, and
 * toString methods.
 * {@code @Builder} is a Lombok annotation to generate a builder pattern for object creation.
 * {@code @AllArgsConstructor} is a Lombok annotation to generate a constructor with all arguments.
 * {@code @NoArgsConstructor} is a Lombok annotation to generate a constructor with no arguments.
 *
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendRequest {

    /**
     * The content of the message to be sent.
     */
    private String content;

}