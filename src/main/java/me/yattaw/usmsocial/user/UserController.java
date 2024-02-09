package me.yattaw.usmsocial.user;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.user.requests.UserRequest;
import me.yattaw.usmsocial.user.requests.UserPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/create_post")
    public ResponseEntity<UserActionResponse> createPost(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createPost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

    @PostMapping("/create_comment")
    public ResponseEntity<UserActionResponse> createComment(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createComment(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

    @PostMapping("/like_post")
    public ResponseEntity<UserActionResponse> likePost(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(service.likePost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }


    @DeleteMapping("/delete_post")
    public ResponseEntity<UserActionResponse> deletePost(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(service.deletePost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }


}
