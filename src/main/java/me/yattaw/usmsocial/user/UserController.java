package me.yattaw.usmsocial.user;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.user.requests.UserDeleteRequest;
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

    @DeleteMapping("/delete_post")
    public ResponseEntity<UserActionResponse> deletePost(
            @RequestBody UserDeleteRequest request
    ) {
        return ResponseEntity.ok(service.deletePost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }


}
