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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
/**
 * Service class for managing message-related operations.
 *
 * <p>This service class provides methods for sending messages to users, retrieving recent messages,
 * and fetching messages between specific users.</p>
 *
 * <p>The service relies on the {@link UserRepository} and {@link DirectMessageRepository}
 * for accessing user and direct message data, respectively.</p>
 *
 * <p>It also uses the {@link JwtService} to handle JSON Web Token operations.</p>
 *
 * {@code @Service} indicates that this class is a service component in the Spring application context.
 * {@code @RequiredArgsConstructor} is a Lombok annotation to generate a constructor with required arguments.
 *
 * @version 17 April 2024
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepository userRepository;
    private final DirectMessageRepository dmRepository;
    private final JwtService jwtService;

    /**
     * Sends a message to the user identified by the provided user ID.
     *
     * <p>Retrieves the user identified by the id path variable and sends a message to the user
     * based on the contents specified in the request body.</p>
     *
     * @param servletRequest The servlet request containing the JWT token.
     * @param request        The request containing the message contents.
     * @param id             The ID of the user to whom the message will be sent.
     * @return UserActionResponse indicating the success or failure of the operation.
     */
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

    /**
     * Retrieves recent messages for the current user.
     *
     * @param request The servlet request containing the JWT token.
     * @return List of RecentMessageInfo representing recent messages.
     */ 
        public List<RecentMessageInfo> getRecentMessages(HttpServletRequest request) {

        String token = jwtService.extractToken(request);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return Collections.emptyList();
        }


        List<User> usersSent = dmRepository.findUsersWithMessagesToUser(user.get());
        List<User> usersFrom = dmRepository.findUsersWithMessagesFromUser(user.get());
        List<User> users = new ArrayList<>();
        users.addAll(usersSent);
        for (User userFrom : usersFrom) {
                if (!users.contains(userFrom)) {
                        users.add(userFrom);
                }
        }
        List<RecentMessageInfo> recentMessages = new ArrayList<>();
        users.forEach(messageUser -> {
            Optional<List<DirectMessage>> message = dmRepository.findMessagesBetweenUsers(
                    user.get(),
                    messageUser,
                    Pageable.ofSize(1)
            );
            message.ifPresent(dm -> recentMessages.add(
                    RecentMessageInfo.builder()
                            .userId(messageUser.getId())
                            .firstName(messageUser.getFirstName())
                            .lastName(messageUser.getLastName())
                            .tagLine(messageUser.getTagLine())
                            .base64Image(messageUser.getBase64ProfilePicture())
                            .lastMessage(dm.get(0).getMessage())
                            .lastSenderFullName(
                                    dm.get(0).getSender().getFirstName() + " " + dm.get(0).getSender().getLastName()
                            )
                            .lastSenderId(dm.get(0).getSender().getId())
                            .timestamp(dm.get(0).getTimestamp())
                            .build()
            ));
        });
        recentMessages.sort(new Comparator<RecentMessageInfo>() {
                public int compare(RecentMessageInfo o1, RecentMessageInfo o2) {
                        return o1.getTimestamp().compareTo(o2.getTimestamp());
                }
        });
        return recentMessages;
    }

    /**
     * Retrieves messages between the current user and a specified user.
     *
     * @param request   The servlet request containing the JWT token.
     * @param senderId  The ID of the sender user.
     * @return List of MessageResponse representing messages between the users.
     */
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

        return messageResponses;
    }
}