package me.yattaw.usmsocial.messages.group;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.messages.request.MessageSendRequest;
import me.yattaw.usmsocial.post.request.UserPostRequest;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class GroupMessageController {

    private final GroupMessageService service;

    @GetMapping("/fetch/group/{groupId}")
    public ResponseEntity<String> getGroupMessages(
            @PathVariable String groupId
    ) {
        return ResponseEntity.ok("not implemented yet");
    }

    /**
     * Sends a message to a group identified by the provided groupId.
     *
     * <p> Retrieves the group identified by the groupId path variable and adds the message contents
     * specified in the request body to the group.</p>
     *
     * @param groupId The ID of the group to which the message will be sent.
     * @param request The request containing the message contents.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @PostMapping("/group/{groupId}")
    public ResponseEntity<String> messageGroup(@PathVariable String groupId, @RequestBody MessageSendRequest request) {
        // Implementation pending
        return ResponseEntity.ok("not implemented yet");
    }

    @PostMapping("/create/group")
    public ResponseEntity<UserActionResponse> createGroup(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createGroup(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), request)
        );
    }

}
