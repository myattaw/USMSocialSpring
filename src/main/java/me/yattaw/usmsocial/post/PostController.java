package me.yattaw.usmsocial.post;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import me.yattaw.usmsocial.post.request.UserPostRequest;
import me.yattaw.usmsocial.user.requests.UserRequest;
import me.yattaw.usmsocial.post.response.PostFormatResponse;
import me.yattaw.usmsocial.post.response.PostNewInfoResponse;
import me.yattaw.usmsocial.post.response.PostNewResponse;
import me.yattaw.usmsocial.post.response.PostResponse;
import me.yattaw.usmsocial.post.response.PostUserCountResponse;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Control the functions of posts, including creating, deleting, liking,
 * and unliking posts and getting new or recommended posts for user's feeds.
 *
 *  {@code @RestController} indicates that this class is a REST controller.
 *  {@code @RequestMapping("/api/v1/message")} specifies the base URL path for mapping requests handled
 *  by this controller.
 *  {@code @RequiredArgsConstructor} is a Lombok annotation to generate a constructor with required
 *  arguments.
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    /**
     * Creates a new post.
     *
     * <p>This endpoint allows users to create a new post with the content provided
     * in the request body.</p>
     *
     * @param request The request containing the post content.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @PostMapping("/create")
    public ResponseEntity<UserActionResponse> createPost(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createPost(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

    /**
     * Creates a new comment on a post.
     *
     * <p>This endpoint allows users to create a new comment on a post with the content provided in the request body.</p>
     *
     * @param request The request containing the comment content.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
    @PostMapping("/comment")
    public ResponseEntity<UserActionResponse> createComment(
            @RequestBody UserPostRequest request
    ) {
        return ResponseEntity.ok(service.createComment(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                request)
        );
    }

    /**
     * Retrieves recommended posts.
     *
     * <p>This endpoint retrieves recommended posts based on the specified criteria such as date, page number, and page size.</p>
     *
     * @param dateTime   The date and time for retrieving recommended posts.
     * @param pageNumber The page number for pagination.
     * @param pageSize   The size of each page for pagination.
     * @return ResponseEntity containing recommended posts if the operation was successful.
     */
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

    /**
     * Retrieves new recommended posts.
     *
     * <p>This endpoint retrieves new recommended posts based on the last fetch date and time.</p>
     *
     * @param lastFetchDateTime The last fetch date and time for retrieving new recommended posts.
     * @return ResponseEntity containing new recommended posts if the operation was successful.
     */
    @GetMapping("new/recommended")
    public ResponseEntity<ResponseEntity<PostNewResponse>> getNewRecommendedPosts(
          @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime){
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewRecommendedPosts(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                lastFetchDateTime, serverLocalTime));
    }

    /**
     * Retrieves the count of new recommended posts.
     *
     * <p>This endpoint retrieves the count of new recommended posts based on the last fetch date and time.</p>
     *
     * @param lastFetchDateTime The last fetch date and time for retrieving the count of new recommended posts.
     * @return ResponseEntity containing the count of new recommended posts if the operation was successful.
     */
    @GetMapping("new/fetch/recommended")
    public ResponseEntity<ResponseEntity<PostNewInfoResponse>> getNewRecommendedPostsCount(
          @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime){
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewRecommendedPostsCount(lastFetchDateTime, serverLocalTime));
    }

    /**
     * Retrieves the content of a post.
     *
     * <p>This endpoint retrieves the content of a post identified by the post ID.</p>
     *
     * @param id The ID of the post to retrieve.
     * @return ResponseEntity containing the post content if the operation was successful.
     */
    @GetMapping("/session/{id}")
    public ResponseEntity<ResponseEntity<PostFormatResponse>> getPostContent(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getPost(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(), id));
    }

    /**
     * Retrieves posts created by a specific user.
     *
     * <p>This endpoint retrieves posts created by a user identified by the user ID, based on specified criteria such as date, page number, and page size.</p>
     *
     * @param id          The ID of the user.
     * @param dateTime    The date and time for retrieving user posts.
     * @param pageNumber  The page number for pagination.
     * @param pageSize    The size of each page for pagination.
     * @return ResponseEntity containing user posts if the operation was successful.
     */
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

    /**
     * Retrieves new posts created by a specific user.
     *
     * <p>This endpoint retrieves new posts created by a user identified by the user ID, based on the last fetch date and time.</p>
     *
     * @param id                   The ID of the user.
     * @param lastFetchDateTime    The last fetch date and time for retrieving new user posts.
     * @return ResponseEntity containing new user posts if the operation was successful.
     */
    @GetMapping("/new/user/{id}")
    public ResponseEntity<ResponseEntity<PostNewResponse>> getNewUserPosts(@PathVariable Integer id,
        @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime) {
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewUserPost(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(),
                id, lastFetchDateTime, serverLocalTime));
    }

    /**
     * Retrieves the count of new posts created by a specific user.
     *
     * <p>This endpoint retrieves the count of new posts created by a user identified by the user ID, based on the last fetch date and time.</p>
     *
     * @param id                   The ID of the user.
     * @param lastFetchDateTime    The last fetch date and time for retrieving the count of new user posts.
     * @return ResponseEntity containing the count of new user posts if the operation was successful.
     */
    @GetMapping("/new/fetch/user/{id}")
    public ResponseEntity<ResponseEntity<PostNewInfoResponse>> getNewUserPostsCount(@PathVariable Integer id,
        @RequestParam(name = "lastFetchDateTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastFetchDateTime) {
        
        LocalDateTime serverLocalTime = LocalDateTime.now();

        return ResponseEntity.ok(service.getNewUserPostCount(id, lastFetchDateTime, serverLocalTime));
    }

    /**
     * Retrieves the count of posts created by a specific user.
     *
     * <p>This endpoint retrieves the count of posts created by a user identified by the user ID.</p>
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the count of user posts if the operation was successful.
     */
    @GetMapping("/count/user/{id}")
    public ResponseEntity<ResponseEntity<PostUserCountResponse>> getUserPostCount(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getUserPostCount(id));
    }

    /**
     * Likes a post.
     *
     * <p>This endpoint allows users to like a post identified by the post ID.</p>
     *
     * @param request The request containing the post ID.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
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

    /**
     * Unlikes a post.
     *
     * <p>This endpoint allows users to unlike a post identified by the post ID.</p>
     *
     * @param request The request containing the post ID.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
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

    /**
     * Deletes a post.
     *
     * <p>This endpoint allows users to delete a post identified by the post ID.</p>
     *
     * @param request The request containing the post ID.
     * @return ResponseEntity containing a success message if the operation was successful.
     */
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
