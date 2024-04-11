package me.yattaw.usmsocial.messages;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.message.DirectMessage;
import me.yattaw.usmsocial.entities.message.GroupMessage;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.messages.request.MessageSendRequest;
import me.yattaw.usmsocial.repositories.DirectMessageRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

}