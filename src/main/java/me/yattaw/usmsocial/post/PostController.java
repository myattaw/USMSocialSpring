package me.yattaw.usmsocial.post;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import me.yattaw.usmsocial.post.request.UserPostRequest;
import me.yattaw.usmsocial.user.requests.UserRequest;
import me.yattaw.usmsocial.post.response.PostNewInfoResponse;
import me.yattaw.usmsocial.post.response.PostNewResponse;
import me.yattaw.usmsocial.post.response.PostResponse;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @PostMapping("/create")
    public ResponseEntity<UserActionResponse> createPost(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createPost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

    @PostMapping("/comment")
    public ResponseEntity<UserActionResponse> createComment(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createComment(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

    @GetMapping("/recommended")
    public ResponseEntity<ResponseEntity<PostResponse>> getRecommendedPosts(
                @RequestParam(name = "datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                @RequestParam Integer pageNumber,
                @RequestParam Integer pageSize) {
        if (dateTime == null) {
                dateTime = LocalDateTime.now();
        }
        
        return ResponseEntity.ok(service.getRecommendedPosts(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), 
                        dateTime, pageNumber, pageSize));
    }

    @GetMapping("new/recommended")
    public ResponseEntity<ResponseEntity<PostNewResponse>> getNewRecommendedPosts(
          @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime){
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewRecommendedPosts(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                lastFetchDateTime, serverLocalTime));
    }

    @GetMapping("new/fetch/recommended")
    public ResponseEntity<ResponseEntity<PostNewInfoResponse>> getNewRecommendedPostsCount(
          @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime){
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewRecommendedPostsCount(lastFetchDateTime, serverLocalTime));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseEntity<PostResponse>> getUserPosts(@PathVariable Integer id, 
                @RequestParam(name = "datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                @RequestParam Integer pageNumber,
                @RequestParam Integer pageSize) {
        if (dateTime == null) {
                dateTime = LocalDateTime.now();
        }

        return ResponseEntity.ok(service.getUserPosts(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                id, dateTime, pageNumber, pageSize));
    }

    @GetMapping("/new/user/{id}")
    public ResponseEntity<ResponseEntity<PostNewResponse>> getNewUserPosts(@PathVariable Integer id,
        @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime) {
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewUserPost(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                id, lastFetchDateTime, serverLocalTime));
    }

    @GetMapping("/new/fetch/user/{id}")
    public ResponseEntity<ResponseEntity<PostNewInfoResponse>> getNewUserPostsCount(@PathVariable Integer id,
        @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime) {
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewUserPostCount(id, lastFetchDateTime, serverLocalTime));
    }

    @PostMapping("/like")
    public ResponseEntity<UserActionResponse> likePost(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(service.adjustPostLike(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request,
                true)
        );
    }

    @PostMapping("/unlike")
    public ResponseEntity<UserActionResponse> unlikePost(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(service.adjustPostLike(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request,
                false)
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserActionResponse> deletePost(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(service.deletePost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

}
