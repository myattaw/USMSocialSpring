package me.yattaw.usmsocial.admin;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import me.yattaw.usmsocial.repositories.PostRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public AdminActionResponse deleteUser(AdminDeleteRequest request) {
        Optional<User> user = userRepository.findById(request.getTargetId());

        if (user.isPresent()) {
            // Delete or update related posts
            postRepository.deleteByUser(user.get());

            // Now delete the user
            userRepository.delete(user.get());

            return AdminActionResponse.builder()
                    .status(1)
                    .message("User and related posts have been successfully deleted.")
                    .build();
        } else {
            return AdminActionResponse.builder()
                    .status(0)
                    .message("User could not be found.")
                    .build();
        }
    }

    public AdminActionResponse deletePost(AdminDeleteRequest request) {
        Optional<UserPost> userPost = postRepository.findById(request.getTargetId());

        if (userPost.isPresent()) {
            // Delete or update related posts
            postRepository.delete(userPost.get());

            return AdminActionResponse.builder()
                    .status(1)
                    .message("Post has been successfully deleted.")
                    .build();
        } else {
            return AdminActionResponse.builder()
                    .status(0)
                    .message("Post could not be found.")
                    .build();
        }
    }

}
