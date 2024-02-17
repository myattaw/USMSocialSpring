package me.yattaw.usmsocial.post;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.user.UserActionResponse;
import me.yattaw.usmsocial.user.requests.UserPostRequest;
import me.yattaw.usmsocial.user.requests.UserRequest;
import me.yattaw.usmsocial.user.responses.RecommendedPostResponse;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ResponseEntity<Page<RecommendedPostResponse>>> getRecommendedPosts() {
        return ResponseEntity.ok(service.getRecommendedPosts());
    }

    @PostMapping("/like")
    public ResponseEntity<UserActionResponse> likePost(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(service.likePost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
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
