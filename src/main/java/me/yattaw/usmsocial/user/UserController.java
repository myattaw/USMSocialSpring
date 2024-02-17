package me.yattaw.usmsocial.user;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.user.responses.RecommendedPostResponse;
import me.yattaw.usmsocial.user.requests.UserRequest;
import me.yattaw.usmsocial.user.requests.UserPostRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping("/profile_picture")
    public ResponseEntity<String> changeProfilePicture( ) {
        return ResponseEntity.ok("not implemented yet");
    }

    @PostMapping("/follow/{id}")
    public ResponseEntity<String> followUser(@PathVariable String id) {
        return ResponseEntity.ok("not implemented yet");
    }

    @DeleteMapping("/unfollow/{id}")
    public ResponseEntity<String> unfollowUser(@PathVariable String id) {
        return ResponseEntity.ok("not implemented yet");
    }

    @PostMapping("/message/{id}")
    public ResponseEntity<String> messageUser(@PathVariable String id) {
        return ResponseEntity.ok("not implemented yet");
    }

}
