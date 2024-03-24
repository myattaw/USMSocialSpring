package me.yattaw.usmsocial.user;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.report.UserReportRequest;
import me.yattaw.usmsocial.entities.user.UserInfo;
import me.yattaw.usmsocial.user.requests.UserInfoRequest;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import me.yattaw.usmsocial.user.responses.UserInfoResponse;
import me.yattaw.usmsocial.user.responses.UserSearchResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping("/profile_picture")
    public ResponseEntity<UserActionResponse> changeProfilePicture(@RequestPart("imageData") MultipartFile imageData) throws IOException {
        return ResponseEntity.ok(service.uploadProfilePicture(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                imageData.getBytes()
        ));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getUserInfo(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), id));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInfoResponse> getUserProfile() {
        return ResponseEntity.ok(service.getProfileUserInfo(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()));
    }

    @GetMapping("/search")
    public ResponseEntity<UserSearchResponse> getUserSearch(
            @RequestParam String query,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize) {
        return ResponseEntity.ok(service.getUserSearch(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), query, pageNumber, pageSize));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserActionResponse> patchUserProfile(@RequestBody UserInfoRequest request) {
        return ResponseEntity.ok(service.setProfileUserInfo(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
                new UserInfo(request.getId(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getTagLine(), request.getBio())));
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

    @PostMapping("/report")
    public ResponseEntity<UserActionResponse> reportId(@RequestBody UserReportRequest request) {
        return ResponseEntity.ok(
                service.reportId(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

}
