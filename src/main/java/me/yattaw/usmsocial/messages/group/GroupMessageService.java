package me.yattaw.usmsocial.messages.group;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.message.GroupMessage;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserGroups;
import me.yattaw.usmsocial.messages.request.MessageSendRequest;
import me.yattaw.usmsocial.post.request.UserPostRequest;
import me.yattaw.usmsocial.repositories.GroupMessageRepository;
import me.yattaw.usmsocial.repositories.UserGroupRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupMessageService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final JwtService jwtService;

    public UserActionResponse createGroup(
            HttpServletRequest servletRequest,
            UserPostRequest request
    ) {

        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

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
                .name(request.getContent().isEmpty() ?
                        "Untitled Group" : request.getContent()
                )
                .members(groupMembers)
                .timestamp(LocalDateTime.now())
                .build();

        userGroupRepository.save(group);

        return UserActionResponse.builder()
                .status(1)
                .message("Post has been created successfully!")
                .build();
    }

    public UserActionResponse inviteGroupMember(
            HttpServletRequest servletRequest,
            Integer groupId,
            Integer userId
    ) {
        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

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
                    .message(String.format(
                                    "Successfully invited %s %s to '%s' group.",
                                    invitedUser.get().getFirstName(),
                                    invitedUser.get().getLastName(),
                                    group.get().getName()
                            )
                    )
                    .build();
        }

        return UserActionResponse.builder()
                .status(1)
                .message(String.format("Failed at inviting user_id %d to group_id %d.", userId, groupId))
                .build();
    }

    public UserActionResponse messageGroup(
            HttpServletRequest servletRequest,
            MessageSendRequest request,
            Integer id
    ) {
        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

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
