package me.yattaw.usmsocial.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import me.yattaw.usmsocial.repositories.PostRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.requests.UserDeleteRequest;
import me.yattaw.usmsocial.user.requests.UserPostRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserActionResponse createPost(
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

        UserPost userPost = UserPost.builder()
                .user(user.get())
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        postRepository.save(userPost);

        return UserActionResponse.builder()
                .status(1)
                .message("Post has been created successfully!")
                .build();

    }

    public UserActionResponse deletePost(
            HttpServletRequest servletRequest,
            UserDeleteRequest request
    ) {

        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        Optional<UserPost> userPost = postRepository.findById(request.getTargetId());

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        if (userPost.isPresent()) {
            if (userPost.get().getUser().equals(user.get())) {
                postRepository.delete(userPost.get());
                return UserActionResponse.builder()
                        .status(1)
                        .message("Post has been deleted successfully!")
                        .build();
            } else {
                return UserActionResponse.builder()
                        .status(0)
                        .message("You are not authorized to delete this post.")
                        .build();
            }
        } else {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to find user post.")
                    .build();
        }
    }

}
