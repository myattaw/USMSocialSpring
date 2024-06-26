package me.yattaw.usmsocial.post;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.auth.AuthenticationService;
import me.yattaw.usmsocial.entities.post.PostComment;
import me.yattaw.usmsocial.entities.post.PostLike;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import me.yattaw.usmsocial.repositories.CommentRepository;
import me.yattaw.usmsocial.repositories.LikeRepository;
import me.yattaw.usmsocial.repositories.PostRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.AuthenicationException;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import me.yattaw.usmsocial.post.request.UserPostRequest;
import me.yattaw.usmsocial.user.requests.UserRequest;
import me.yattaw.usmsocial.post.response.PostCommentResponse;
import me.yattaw.usmsocial.post.response.PostFormatResponse;
import me.yattaw.usmsocial.post.response.PostNewInfoResponse;
import me.yattaw.usmsocial.post.response.PostNewResponse;
import me.yattaw.usmsocial.post.response.PostResponse;
import me.yattaw.usmsocial.post.response.PostUserCountResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class that handles operations related to posts.
 * <p>
 * This class is annotated with {@code @Service} to indicate that it is a Spring-managed service component.
 * </p>
 *
 * <p>
 * The {@code @RequiredArgsConstructor} annotation is used to automatically generate a constructor
 * that initializes all final fields with arguments.
 * </p>
 *
 * @version 17 April 2024
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    /**
     * Retrieves the current user from the request.
     *
     * @param servletRequest The servlet request containing the user token.
     * @return Optional of the current user if found, otherwise empty.
     */
    private Optional<User> getCurrentUser(HttpServletRequest servletRequest) {
        String token = jwtService.extractToken(servletRequest);
        return userRepository.findByEmail(jwtService.fetchEmail(token));
    }

    /**
     * Updates the 'liked' status of posts for the current user.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param posts           The posts to update.
     */
    public void updateIsLiked(HttpServletRequest servletRequest, Iterable<PostFormatResponse> posts) {
        Optional<User> user = getCurrentUser(servletRequest);

        if (user.isEmpty() || user.get().getRole() == Role.GUEST) {
                throw new AuthenicationException("Only users can access");
        }

        for (PostFormatResponse post : posts) {
                Integer count = likeRepository.getCountOfUserAndPostLikes(user.get().getId(), post.getId());
                post.setLiked((count > 0));
        }
    }

    /**
     * Creates a new post.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param request         The request containing the post content.
     * @return UserActionResponse indicating the result of the operation.
     */
    public UserActionResponse createPost(
            HttpServletRequest servletRequest,
            UserPostRequest request
    ) {

        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        UserPost userPost = UserPost.builder()
                .user(user.get())
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        postRepository.save(userPost);

        return UserActionResponse.builder()
                .status(1)
                .message("Post has been created successfully!")
                .build();

    }

    /**
     * Creates a new comment on a post.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param request         The request containing the comment content.
     * @return UserActionResponse indicating the result of the operation.
     */
    public UserActionResponse createComment(
            HttpServletRequest servletRequest,
            UserPostRequest request
    ) {

        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        Optional<UserPost> userPost = postRepository.findById(request.getId());

        if (userPost.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to find user post.")
                    .build();
        }

        PostComment postComment = PostComment.builder()
                .post(userPost.get())
                .user(user.get())
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        commentRepository.save(postComment);

        return UserActionResponse.builder()
                .status(1)
                .message("Comment has been created successfully!")
                .build();

    }

    /**
     * Retrieves the content of a post.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param id             The ID of the post to retrieve.
     * @return ResponseEntity containing the post content.
     */
    public ResponseEntity<PostFormatResponse> getPost(HttpServletRequest servletRequest, Integer id) {
        authenticationService.isAuthorizedAccess(servletRequest);
        UserPost post = postRepository.getReferenceById(id);
        PostFormatResponse postReturn = this.mapToSimplifiedPostResponse(post);

        return ResponseEntity.ok(postReturn);
    }

    /**
     * Retrieves recommended posts.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param dateTime       The date and time for retrieving recommended posts.
     * @param pageNumber     The page number for pagination.
     * @param pageSize       The size of each page for pagination.
     * @return ResponseEntity containing recommended posts.
     */
    public ResponseEntity<PostResponse> getRecommendedPosts(
        HttpServletRequest servletRequest,
        LocalDateTime dateTime, Integer pageNumber, Integer pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<UserPost> postsResult = postRepository.getRecommended(dateTime, pageRequest);
        Page<PostFormatResponse> posts = postsResult.map(this::mapToSimplifiedPostResponse);

        updateIsLiked(servletRequest, posts);

        return ResponseEntity.ok(
                PostResponse.builder()
                        .pageResult(posts)
                        .dateTimeFetch(dateTime)
                        .build());
    }

    /**
     * Retrieves new recommended posts.
     *
     * @param servletRequest  The servlet request containing the user token.
     * @param fetchDataTime   The last fetch date and time for retrieving new recommended posts.
     * @param serverDateTime  The current server date and time.
     * @return ResponseEntity containing new recommended posts.
     */
    public ResponseEntity<PostNewResponse> getNewRecommendedPosts(
        HttpServletRequest servletRequest,
        LocalDateTime fetchDataTime,
        LocalDateTime serverDateTime
    ) {
        List<UserPost> posts = postRepository.getNewRecommendedPosts(serverDateTime, fetchDataTime);
        List<PostFormatResponse> postsResult = posts.stream().map(this::mapToSimplifiedPostResponse).collect(Collectors.toList());

        updateIsLiked(servletRequest, postsResult);

        return ResponseEntity.ok(
                PostNewResponse.builder()
                        .posts(postsResult)
                        .serverDateTime(serverDateTime)
                        .build());
    }

    /**
     * Retrieves the count of new recommended posts.
     *
     * @param fetchDataTime  The last fetch date and time for retrieving the count of new recommended posts.
     * @param serverDateTime The current server date and time.
     * @return ResponseEntity containing the count of new recommended posts.
     */
    public ResponseEntity<PostNewInfoResponse> getNewRecommendedPostsCount(
        LocalDateTime fetchDataTime,
        LocalDateTime serverDateTime
    ) {
        Integer newCount = postRepository.getNewRecommendedPostsCount(serverDateTime, fetchDataTime);

        return ResponseEntity.ok(PostNewInfoResponse.builder()
                        .amountOfNewPost(newCount)
                        .lastFetchDateTime(fetchDataTime)
                        .serverDateTime(serverDateTime).build());
    }

    /**
     * Retrieves posts created by a specific user.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param userId         The ID of the user.
     * @param dateTime       The date and time for retrieving user posts.
     * @param pageNumber     The page number for pagination.
     * @param pageSize       The size of each page for pagination.
     * @return ResponseEntity containing user posts.
     */
    public ResponseEntity<PostResponse> getUserPosts(
                HttpServletRequest servletRequest,
                Integer userId, LocalDateTime dateTime, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<UserPost> posts = postRepository.getUserPosts(userId, dateTime, pageRequest);
        Page<PostFormatResponse> pageResult = posts.map(this::mapToSimplifiedPostResponse);

        updateIsLiked(servletRequest, pageResult);
        return ResponseEntity.ok(PostResponse.builder().pageResult(pageResult).dateTimeFetch(dateTime).build());
    }

    /**
     * Retrieves new posts created by a specific user.
     *
     * @param servletRequest  The servlet request containing the user token.
     * @param userId          The ID of the user.
     * @param fetchDataTime   The last fetch date and time for retrieving new user posts.
     * @param serverDateTime  The current server date and time.
     * @return ResponseEntity containing new user posts.
     */
    public ResponseEntity<PostNewResponse> getNewUserPost(
        HttpServletRequest servletRequest,
        Integer userId,
        LocalDateTime fetchDataTime,
        LocalDateTime serverDateTime) {
        List<UserPost> posts = postRepository.getNewUserPosts(userId, serverDateTime, fetchDataTime);
        List<PostFormatResponse> postsResult = posts.stream().map(this::mapToSimplifiedPostResponse).collect(Collectors.toList());
        
        updateIsLiked(servletRequest, postsResult);

        return ResponseEntity.ok(
                PostNewResponse.builder()
                        .posts(postsResult)
                        .serverDateTime(serverDateTime)
                        .build());
    }

    /**
     * Retrieves the count of new posts created by a specific user.
     *
     * @param userId          The ID of the user.
     * @param fetchDataTime   The last fetch date and time for retrieving the count of new user posts.
     * @param serverDateTime  The current server date and time.
     * @return ResponseEntity containing the count of new user posts.
     */
    public ResponseEntity<PostNewInfoResponse> getNewUserPostCount(
        Integer userId,
        LocalDateTime fetchDataTime,
        LocalDateTime serverDateTime
    ) {
        Integer newCount = postRepository.getNewUserPostsCount(userId, serverDateTime, fetchDataTime);

        return ResponseEntity.ok(PostNewInfoResponse.builder()
                        .amountOfNewPost(newCount)
                        .lastFetchDateTime(fetchDataTime)
                        .serverDateTime(serverDateTime).build());
    }

    /**
     * Retrieves the count of posts created by a specific user.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity containing the count of user posts.
     */
    public ResponseEntity<PostUserCountResponse> getUserPostCount(
            Integer userId
    ) {
            Integer count = postRepository.getUserPostCount(userId);

            return ResponseEntity.ok(PostUserCountResponse.builder().count(count).build());
    }

    //helper method
    private PostFormatResponse mapToSimplifiedPostResponse(UserPost post) {

        return PostFormatResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .postUserInfo(post.getUser().getPostUserInfo())
                //TODO: make the code only fetch 10 comments at a time
                .comments(
                        post.getComments().stream().map(comment -> PostCommentResponse.builder()
                                .commentId(comment.getCommentId())
                                .content(comment.getContent())
                                .commenterFirstName(comment.getUser().getFirstName())
                                .commenterLastName(comment.getUser().getLastName())
                                .profilePictureBase64(comment.getUser().getBase64ProfilePicture())
                                .timestamp(comment.getTimestamp())
                                .build()).collect(Collectors.toCollection(() -> new ArrayList<>(post.getComments().size()))
                        ))
                .isLiked(false)
                .timestamp(post.getTimestamp())
                .likeCount(postRepository.getLikeCount(post.getId())) // Find a better way of doing this if possible
                .build();
    }

    /**
     * Adjusts the like status of a post.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param request         The request containing the post ID and like action.
     * @param addLike         A boolean indicating whether to add or remove the like.
     * @return UserActionResponse indicating the result of the operation.
     */
    public UserActionResponse adjustPostLike(
            HttpServletRequest servletRequest,
            UserRequest request,
            boolean addLike
    ) {

        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        Optional<UserPost> userPost = postRepository.findById(request.getTargetId());
        if (userPost.isPresent()) {
            // Check if the user has already liked the post
            if (userPost.get().getLikes().stream().anyMatch(like -> like.getUser().equals(user.get()))) {
                if (addLike) {
                    return UserActionResponse.builder()
                            .status(0)
                            .message("User has already liked this post.")
                            .build();
                } else {
                    for (PostLike like : userPost.get().getLikes()) {
                        if (like.getUser().equals(user.get())) {
                            userPost.get().removeLike(like);
                            likeRepository.delete(like);
                            return UserActionResponse.builder()
                                    .status(1)
                                    .message("User has successfully removed like to post.")
                                    .build();
                        }
                    }
                }
            }

            PostLike postLike = PostLike.builder()
                    .user(user.get())
                    .post(userPost.get())
                    .timestamp(LocalDateTime.now())
                    .build();

            userPost.get().addLike(postLike);
            likeRepository.save(postLike);

            return UserActionResponse.builder()
                    .status(1)
                    .message("User has successfully added like to post.")
                    .build();
        }

        return UserActionResponse.builder()
                .status(0)
                .message("Could not find post to like.")
                .build();
    }

    /**
     * Deletes a post.
     *
     * @param servletRequest The servlet request containing the user token.
     * @param request         The request containing the post ID.
     * @return UserActionResponse indicating the result of the operation.
     */
    public UserActionResponse deletePost(
            HttpServletRequest servletRequest,
            UserRequest request
    ) {

        String token = jwtService.extractToken(servletRequest);

        Optional<User> user = userRepository.findByEmail(
                jwtService.fetchEmail(token)
        );

        Optional<UserPost> userPost = postRepository.findById(request.getTargetId());

        // This should only happen if a user was deleted
        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        if (userPost.isPresent()) {
            if (userPost.get().getUser().equals(user.get())) {
                // Delete posts and all likes and comments attached to post
                commentRepository.deleteAll(userPost.get().getComments());
                likeRepository.deleteAll(userPost.get().getLikes());
                postRepository.delete(userPost.get());

                return UserActionResponse.builder()
                        .status(1)
                        .message("Post has been deleted successfully!")
                        .build();
            } else {
                return UserActionResponse.builder()
                        .status(0)
                        .message("You are not authorized to delete this post.")
                        .build();
            }
        } else {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to find user post.")
                    .build();
        }
    }


}
