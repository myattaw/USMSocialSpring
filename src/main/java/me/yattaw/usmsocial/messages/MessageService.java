package me.yattaw.usmsocial.messages;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.message.DirectMessage;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.messages.request.MessageSendRequest;
import me.yattaw.usmsocial.messages.response.MessageResponse;
import me.yattaw.usmsocial.messages.response.RecentMessageInfo;
import me.yattaw.usmsocial.repositories.DirectMessageRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepository userRepository;
    private final DirectMessageRepository dmRepository;
    private final JwtService jwtService;

    public UserActionResponse messageUser(
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

        Optional<User> targetUser = userRepository.findById(id);
        if (targetUser.isPresent()) {
            DirectMessage directMessage = DirectMessage.builder()
                    .message(request.getContent())
                    .sender(user.get())
                    .receiver(targetUser.get())
                    .timestamp(LocalDateTime.now())
                    .build();

            dmRepository.save(directMessage);
        } else {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to send message to user.")
                    .build();
        }

        return UserActionResponse.builder()
                .status(1)
                .message("User message has been sent successfully!")
                .build();

    }

    public List<RecentMessageInfo> getRecentMessages(HttpServletRequest request) {

        String token = jwtService.extractToken(request);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return Collections.emptyList();
        }


        List<User> users = dmRepository.findUsersWithMessagesFromUser(user.get());
        List<RecentMessageInfo> recentMessages = new ArrayList<>();
        users.forEach(messageUser -> {
            Optional<DirectMessage> message = dmRepository.findLastMessageBetweenUsers(user.get(), messageUser);
            message.ifPresent(dm -> recentMessages.add(
                    RecentMessageInfo.builder()
                            .userId(messageUser.getId())
                            .firstName(messageUser.getFirstName())
                            .lastName(messageUser.getLastName())
                            .tagLine(messageUser.getTagLine())
                            .base64Image(messageUser.getBase64ProfilePicture())
                            .lastMessage(dm.getMessage())
                            .lastSenderFullName(
                                    dm.getSender().getFirstName() + " " + dm.getSender().getLastName()
                            )
                            .lastSenderId(dm.getSender().getId())
                            .timestamp(dm.getTimestamp())
                            .build()
            ));
        });
        return recentMessages;
    }

    public List<MessageResponse> getMessages(HttpServletRequest request, Integer senderId) {

        String token = jwtService.extractToken(request);

        Optional<User> sender = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        Optional<User> receiver = userRepository.findById(senderId);

        // This should only happen if a user was deleted
        if (sender.isEmpty() || receiver.isEmpty()) {
            return Collections.emptyList();
        }

        // Grab the last 100 messages between users
        Optional<List<DirectMessage>> messageList = dmRepository.findMessagesBetweenUsers(
                sender.get(),
                receiver.get(),
                Pageable.ofSize(100)
        );

        List<MessageResponse> messageResponses = new ArrayList<>();
        messageList.ifPresent(directMessages -> directMessages.forEach(dm -> messageResponses.add(
                MessageResponse.builder()
                        .userId(dm.getSender().getId())
                        .firstName(dm.getSender().getFirstName())
                        .lastName(dm.getSender().getLastName())
                        .content(dm.getMessage())
                        .timestamp(dm.getTimestamp())
                        .build()
        )));

        return Collections.emptyList();
    }
}