package me.yattaw.usmsocial.messages.group;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.messages.request.MessageSendRequest;
import me.yattaw.usmsocial.messages.response.MessageResponse;
import me.yattaw.usmsocial.post.request.UserPostRequest;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
/**
 * Controls the messaging operations for groups within the application.
 *
 * <p>This controller class handles HTTP requests related to group messaging operations,
 * such as fetching group messages, sending messages to groups, creating new groups,
 * and inviting members to existing groups.</p>
 *
 * {@code @RestController} indicates that this class is a REST controller.
 * {@code @RequestMapping("/api/v1/message")} specifies the base URL for mapping HTTP
 *        requests to this controller.
 * {@code @RequiredArgsConstructor} generates a constructor with required arguments for
 *        the injected dependencies.
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class GroupMessageController {

    private final GroupMessageService service;

    /**
     * Retrieves messages for a specific group.
     *
     * <p>This method fetches messages belonging to the group identified by the provided groupId.</p>
     *
     * @param groupId The ID of the group for which messages will be retrieved.
     * @return ResponseEntity containing a list of messages if the operation was successful.
     */
    @GetMapping("/fetch/group/{groupId}")
    public ResponseEntity<List<MessageResponse>> getGroupMessages(
            @PathVariable Integer groupId
    ) {
        return ResponseEntity.ok(
                service.getGroupMessages(
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                        groupId)
        );
    }

    /**
     * Sends a message to a group identified by the provided groupId.
     *
     * <p>This method retrieves the group identified by the groupId path variable and adds the message contents
     * specified in the request body to the group.</p>
     *
     * @param groupId The ID of the group to which the message will be sent.
     * @param request The request containing the message contents.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @PostMapping("/group/{groupId}")
    public ResponseEntity<UserActionResponse> messageGroup(
            @RequestBody MessageSendRequest request,
            @PathVariable Integer groupId
    ) {
        return ResponseEntity.ok(service.messageGroup(
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                        request,
                        groupId
                )
        );
    }

    /**
     * Creates a new group based on the provided request.
     *
     * <p>This method creates a new group using the details provided in the request body.</p>
     *
     * @param request The request containing the details of the group to be created.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @PostMapping("/create/group")
    public ResponseEntity<UserActionResponse> createGroup(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createGroup(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), request)
        );
    }

    /**
     * Invites a user to join a specific group.
     *
     * <p>This method sends an invitation to the user identified by the provided userId
     * to join the group identified by the provided groupId.</p>
     *
     * @param groupId The ID of the group to which the user will be invited.
     * @param userId The ID of the user to be invited.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @PostMapping("/invite/{groupId}/{userId}")
    public ResponseEntity<UserActionResponse> inviteGroupMember(
            @PathVariable Integer groupId,
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(service.inviteGroupMember(
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                        groupId,
                        userId
                )
        );
    }
}