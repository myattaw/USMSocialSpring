package me.yattaw.usmsocial.messages;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.messages.request.MessageSendRequest;
import me.yattaw.usmsocial.messages.response.MessageResponse;
import me.yattaw.usmsocial.messages.response.RecentMessageInfo;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
/**
 * Controller for handling message-related operations.
 *
 * <p>This controller provides endpoints for retrieving recent messages, fetching messages for a
 * specific user,
 * and sending messages to users.</p>
 *
 * <p>Endpoints:
 * <ul>
 *     <li>GET /api/v1/message/recent/: Retrieves recent messages.</li>
 *     <li>GET /api/v1/message/fetch/user/{senderId}: Retrieves messages for a specific user.</li>
 *     <li>POST /api/v1/message/user/{id}: Sends a message to a user identified by the provided user
 *     ID.</li>
 * </ul>
 * </p>
 *
 * <p>The controller relies on the {@link MessageService} for handling message-related operations.</p>
 *
 * {@code @RestController} indicates that this class is a REST controller.
 * {@code @RequestMapping("/api/v1/message")} specifies the base URL path for mapping requests handled
 * by this controller.
 * {@code @RequiredArgsConstructor} is a Lombok annotation to generate a constructor with required
 * arguments.
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;

    /**
     * Retrieves recent messages.
     *
     * <p>This endpoint retrieves recent messages for the current user.</p>
     *
     * @return ResponseEntity containing a list of RecentMessageInfo objects representing recent messages.
     */
    @GetMapping("/recent/")
    public ResponseEntity<List<RecentMessageInfo>> getRecentMessages() {
        return ResponseEntity.ok(service.getRecentMessages(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
        ));
    }

    /**
     * Retrieves messages for a specific user identified by the senderId.
     *
     * <p>This endpoint retrieves messages for the user identified by the senderId path variable.
     * The messages can be retrieved based on various criteria such as message history,
     * message type, or any other relevant parameters.</p>
     *
     * @param senderId The ID of the user for whom messages will be retrieved.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @GetMapping("/fetch/user/{senderId}")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable Integer senderId
    ) {
        return ResponseEntity.ok(service.getMessages(
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                        senderId
                )
        );
    }


    /**
     * Sends a message to the user identified by the provided user ID.
     *
     * <p>Retrieves the user identified by the id path variable and sends a message to the user
     * based on the contents specified in the request body.</p>s
     *
     * @param id      The ID of the user to whom the message will be sent.
     * @param request The request containing the message contents.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @PostMapping("/user/{id}")
    public ResponseEntity<UserActionResponse> messageUser(
            @RequestBody MessageSendRequest request,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(service.messageUser(
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                        request,
                        id
                )
        );
    }

}