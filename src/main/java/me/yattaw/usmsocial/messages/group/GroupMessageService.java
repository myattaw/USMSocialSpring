package me.yattaw.usmsocial.messages.group;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.message.GroupMessage;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserGroups;
import me.yattaw.usmsocial.messages.request.MessageSendRequest;
import me.yattaw.usmsocial.messages.response.MessageResponse;
import me.yattaw.usmsocial.post.request.UserPostRequest;
import me.yattaw.usmsocial.repositories.GroupMessageRepository;
import me.yattaw.usmsocial.repositories.UserGroupRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Provides services for handling group messaging operations.
 *
 * <p>This service class contains methods to fetch group messages, send messages to groups,
 * create new groups, and invite members to existing groups.</p>
 *
 * {@code @Service} indicates that this class is a service component in the application.
 * {@code @RequiredArgsConstructor} generates a constructor with required arguments for the
 * injected dependencies.
 *
 * @version 17 April 2024
 */
@Service
@RequiredArgsConstructor
public class GroupMessageService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final JwtService jwtService;

    /**
     * Retrieves messages for a specific group.
     *
     * <p>This method fetches messages belonging to the group identified by the provided groupId.</p>
     *
     * @param servletRequest The HTTP servlet request.
     * @param groupId The ID of the group for which messages will be retrieved.
     * @return List of MessageResponse objects containing messages if the operation was successful.
     */
    public List<MessageResponse> getGroupMessages(HttpServletRequest servletRequest, Integer groupId) {
        String token = jwtService.extractToken(servletRequest);
        Optional<User> user = userRepository.findByEmail(jwtService.fetchEmail(token));

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<UserGroups> userGroup = userGroupRepository.findById(groupId);
        if (userGroup.isPresent()) {
            List<MessageResponse> responseList = new ArrayList<>();
            List<GroupMessage> messages = groupMessageRepository.findTop30ByOrderByTimestampDesc(Pageable.ofSize(30));
            messages.forEach(message -> {
                responseList.add(
                        MessageResponse.builder()
                                .userId(message.getSender().getId())
                                .firstName(message.getSender().getFirstName())
                                .lastName(message.getSender().getLastName())
                                .content(message.getMessage())
                                .timestamp(message.getTimestamp())
                                .build()
                );
            });
            return responseList;
        }

        return Collections.emptyList();
    }

    /**
     * Creates a new group based on the provided request.
     *
     * <p>This method creates a new group using the details provided in the request body.</p>
     *
     * @param servletRequest The HTTP servlet request.
     * @param request The request containing the details of the group to be created.
     * @return UserActionResponse indicating the success or failure of the operation.
     */
    public UserActionResponse createGroup(HttpServletRequest servletRequest, UserPostRequest request) {
        String token = jwtService.extractToken(servletRequest);
        Optional<User> user = userRepository.findByEmail(jwtService.fetchEmail(token));

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        Set<User> groupMembers = new HashSet<>();
        groupMembers.add(user.get());

        UserGroups group = UserGroups.builder()
                .name(request.getContent().isEmpty() ? "Untitled Group" : request.getContent())
                .members(groupMembers)
                .timestamp(LocalDateTime.now())
                .build();

        userGroupRepository.save(group);

        return UserActionResponse.builder()
                .status(1)
                .message("Post has been created successfully!")
                .build();
    }

    /**
     * Invites a user to join a specific group.
     *
     * <p>This method sends an invitation to the user identified by the provided userId
     * to join the group identified by the provided groupId.</p>
     *
     * @param servletRequest The HTTP servlet request.
     * @param groupId The ID of the group to which the user will be invited.
     * @param userId The ID of the user to be invited.
     * @return UserActionResponse indicating the success or failure of the operation.
     */
    public UserActionResponse inviteGroupMember(HttpServletRequest servletRequest, Integer groupId, Integer userId) {
        String token = jwtService.extractToken(servletRequest);
        Optional<User> user = userRepository.findByEmail(jwtService.fetchEmail(token));

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        Optional<UserGroups> group = userGroupRepository.findById(groupId);
        Optional<User> invitedUser = userRepository.findById(userId);

        if (group.isPresent() && invitedUser.isPresent()) {
            group.get().getMembers().add(invitedUser.get());
            return UserActionResponse.builder()
                    .status(1)
                    .message(String.format("Successfully invited %s %s to '%s' group.",
                            invitedUser.get().getFirstName(), invitedUser.get().getLastName(), group.get().getName()))
                    .build();
        }

        return UserActionResponse.builder()
                .status(1)
                .message(String.format("Failed at inviting user_id %d to group_id %d.", userId, groupId))
                .build();
    }

    /**
     * Sends a message to a group identified by the provided groupId.
     *
     * <p>This method retrieves the group identified by the groupId path variable and adds the message contents
     * specified in the request body to the group.</p>
     *
     * @param servletRequest The HTTP servlet request.
     * @param request The request containing the message contents.
     * @param id The ID of the group to which the message will be sent.
     * @return UserActionResponse indicating the success or failure of the operation.
     */
    public UserActionResponse messageGroup(HttpServletRequest servletRequest, MessageSendRequest request, Integer id) {
        String token = jwtService.extractToken(servletRequest);
        Optional<User> user = userRepository.findByEmail(jwtService.fetchEmail(token));

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        Optional<UserGroups> group = userGroupRepository.findById(id);
        if (group.isPresent() && group.get().getMembers().contains(user.get())) {
            GroupMessage groupMessage = GroupMessage.builder()
                    .group(group.get())
                    .message(request.getContent())
                    .sender(user.get())
                    .timestamp(LocalDateTime.now())
                    .build();

            groupMessageRepository.save(groupMessage);
        } else {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to access the group.")
                    .build();
        }

        return UserActionResponse.builder()
                .status(1)
                .message("Group message has been sent successfully!")
                .build();
    }
}
