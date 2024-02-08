package me.yattaw.usmsocial.admin;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.PostRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public AdminActionResponse deleteUser(AdminDeleteUserRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());

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
}
