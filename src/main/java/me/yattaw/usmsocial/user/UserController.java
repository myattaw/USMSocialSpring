package me.yattaw.usmsocial.user;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<UserActionResponse> changeProfilePicture(@RequestBody byte[] imageData) {
        return ResponseEntity.ok(service.uploadProfilePicture(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                imageData
        ));
    }

    @PostMapping("/follow/{id}")
    public ResponseEntity<UserActionResponse> followUser(@PathVariable Integer id) {
        return ResponseEntity.ok(service.followUser(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                id)
        );
    }

    @DeleteMapping("/unfollow/{id}")
    public ResponseEntity<UserActionResponse> unfollowUser(@PathVariable Integer id) {
        return ResponseEntity.ok(service.unfollowUser(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                id)
        );
    }

    @PostMapping("/message/{id}")
    public ResponseEntity<String> messageUser(@PathVariable String id) {
        return ResponseEntity.ok("not implemented yet");
    }

}
