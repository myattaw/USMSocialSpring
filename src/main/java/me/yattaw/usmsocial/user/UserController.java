package me.yattaw.usmsocial.user;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.report.UserReportRequest;
import me.yattaw.usmsocial.entities.user.UserInfo;
import me.yattaw.usmsocial.user.requests.UserInfoRequest;
import me.yattaw.usmsocial.user.requests.UserProfileUpload;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import me.yattaw.usmsocial.user.responses.UserFollowListResponse;
import me.yattaw.usmsocial.user.responses.UserFollowerCountResponse;
import me.yattaw.usmsocial.user.responses.UserFollowingCountResponse;
import me.yattaw.usmsocial.user.responses.UserInfoResponse;
import me.yattaw.usmsocial.user.responses.UserProfilePicture;
import me.yattaw.usmsocial.user.responses.UserSearchResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping("/profile_picture")
    public ResponseEntity<UserActionResponse> changeProfilePicture(
            @RequestBody UserProfileUpload profileUpload) {

        String imageBase64 = profileUpload.getImageBase64();

        if (imageBase64.contains(",")) {
            imageBase64 = imageBase64.split(",")[1];
        }

        byte[] decodedBytes = Base64.getDecoder().decode(imageBase64.getBytes(StandardCharsets.UTF_8));;

        return ResponseEntity.ok(service.uploadProfilePicture(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                decodedBytes
        ));
    }

    @GetMapping("/profile_picture")
    public ResponseEntity<UserProfilePicture> getProfilePicture() {
        return ResponseEntity.ok(service.getProfilePicture(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()));
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
                new UserInfo(request.getId(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getTagLine(), request.getBio(), "")));
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

    @GetMapping("/count/followings/{id}")
    public ResponseEntity<UserFollowingCountResponse> followingsUserCount(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getFollowingsCount(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
                id));
    }

    @GetMapping("/count/followers/{id}")
    public ResponseEntity<UserFollowerCountResponse> followersUserCount(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getFollowersCount(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
                id));
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<UserFollowListResponse> getUserFollowers(
        @PathVariable Integer id,
        @RequestParam Integer pageNumber,
        @RequestParam Integer pageSize) {
        return ResponseEntity.ok(service.getFollowers(
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
            id, 
            pageNumber, 
            pageSize)
        );
    }

    @GetMapping("/followings/{id}")
    public ResponseEntity<UserFollowListResponse> getUserFollowings(
        @PathVariable Integer id,
        @RequestParam Integer pageNumber,
        @RequestParam Integer pageSize) {
        return ResponseEntity.ok(service.getFollowings(
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
            id, 
            pageNumber, 
            pageSize)
        );
    }

    @PostMapping("/report")
    public ResponseEntity<UserActionResponse> reportId(@RequestBody UserReportRequest request) {
        return ResponseEntity.ok(
                service.reportId(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                        request)
        );
    }

}
