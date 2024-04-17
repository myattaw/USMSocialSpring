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

/**
 * Controller class that handles user-related HTTP requests.
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /**
     * Changes the profile picture of the user.
     *
     * @param profileUpload The profile picture upload request.
     * @return ResponseEntity containing the action response.
     */
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

    /**
     * Retrieves the profile picture of the user.
     *
     * @return ResponseEntity containing the profile picture.
     */
    @GetMapping("/profile_picture")
    public ResponseEntity<UserProfilePicture> getProfilePicture() {
        return ResponseEntity.ok(service.getProfilePicture(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()));
    }

    /**
     * Retrieves user information by user ID.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the user information response.
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getUserInfo(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), id));
    }

    /**
     * Retrieves user profile information.
     *
     * @return ResponseEntity containing the user profile information response.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserInfoResponse> getUserProfile() {
        return ResponseEntity.ok(service.getProfileUserInfo(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()));
    }

    /**
     * Searches for users based on the provided query string.
     *
     * @param query      The query string for user search.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The page size for pagination.
     * @return ResponseEntity containing the user search response.
     */
    @GetMapping("/search")
    public ResponseEntity<UserSearchResponse> getUserSearch(
            @RequestParam String query,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize) {
        return ResponseEntity.ok(service.getUserSearch(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), query, pageNumber, pageSize));
    }

    /**
     * Updates user profile information.
     *
     * @param request The user profile update request.
     * @return ResponseEntity containing the action response.
     */
    @PutMapping("/profile")
    public ResponseEntity<UserActionResponse> patchUserProfile(@RequestBody UserInfoRequest request) {
        return ResponseEntity.ok(service.setProfileUserInfo(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                new UserInfo(request.getId(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getTagLine(), request.getBio(), "")));
    }

    /**
     * Follows a user with the specified ID.
     *
     * @param id The ID of the user to follow.
     * @return ResponseEntity containing the action response.
     */
    @PostMapping("/follow/{id}")
    public ResponseEntity<UserActionResponse> followUser(@PathVariable Integer id) {
        return ResponseEntity.ok(service.followUser(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                id)
        );
    }

    /**
     * Unfollows a user with the specified ID.
     *
     * @param id The ID of the user to unfollow.
     * @return ResponseEntity containing the action response.
     */
    @DeleteMapping("/unfollow/{id}")
    public ResponseEntity<UserActionResponse> unfollowUser(@PathVariable Integer id) {
        return ResponseEntity.ok(service.unfollowUser(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                id)
        );
    }

    /**
     * Retrieves the count of users followed by the user with the specified ID.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the following count response.
     */
    @GetMapping("/count/followings/{id}")
    public ResponseEntity<UserFollowingCountResponse> followingsUserCount(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getFollowingsCount(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
                id));
    }

    /**
     * Retrieves the count of followers of the user with the specified ID.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the follower count response.
     */
    @GetMapping("/count/followers/{id}")
    public ResponseEntity<UserFollowerCountResponse> followersUserCount(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getFollowersCount(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
                id));
    }

    /**
     * Retrieves the list of followers of the user with the specified ID.
     *
     * @param id         The ID of the user.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The page size for pagination.
     * @return ResponseEntity containing the user follow list response.
     */
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

    /**
     * Retrieves the list of users followed by the user with the specified ID.
     *
     * @param id         The ID of the user.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The page size for pagination.
     * @return ResponseEntity containing the user follow list response.
     */
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

    /**
     * Reports a user based on the specified report request.
     *
     * @param request The user report request.
     * @return ResponseEntity containing the action response.
     */
    @PostMapping("/report")
    public ResponseEntity<UserActionResponse> reportId(@RequestBody UserReportRequest request) {
        return ResponseEntity.ok(
                service.reportId(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                        request)
        );
    }

}
