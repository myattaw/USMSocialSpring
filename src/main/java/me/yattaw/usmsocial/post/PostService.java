package me.yattaw.usmsocial.post;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    private final JwtService jwtService;

    private Optional<User> getCurrentUser(HttpServletRequest servletRequest) {
        String token = jwtService.extractToken(servletRequest);
        return userRepository.findByEmail(jwtService.fetchEmail(token));
    }

    public void updateIsLiked(HttpServletRequest servletRequest, Iterable<PostFormatResponse> posts) {
        Optional<User> user = getCurrentUser(servletRequest);

        if (user.isEmpty() || user.get().getRole() == Role.GUEST) {
                throw new AuthenicationException("Only users can access");
        }

        for (PostFormatResponse post : posts) {
                Integer count = likeRepository.getCountOfUserAndPostLikes(user.get().getId(), post.getId());
                System.out.println("Fetched");
                System.out.println(count);
                post.setLiked((count > 0));
        }
    }

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

    public ResponseEntity<PostResponse> getUserPosts(
                HttpServletRequest servletRequest,
                Integer userId, LocalDateTime dateTime, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<UserPost> posts = postRepository.getUserPosts(userId, dateTime, pageRequest);
        Page<PostFormatResponse> pageResult = posts.map(this::mapToSimplifiedPostResponse);

        updateIsLiked(servletRequest, pageResult);
        return ResponseEntity.ok(PostResponse.builder().pageResult(pageResult).dateTimeFetch(dateTime).build());
    }

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
                                .timestamp(comment.getTimestamp())
                                .build()).collect(Collectors.toCollection(() -> new ArrayList<>(post.getComments().size()))
                        ))
                .isLiked(false)
                .timestamp(post.getTimestamp())
                .likeCount(postRepository.getLikeCount(post.getId())) // Find a better way of doing this if possible
                .build();
    }

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
