package me.yattaw.usmsocial.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.post.PostComment;
import me.yattaw.usmsocial.entities.post.PostLike;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import me.yattaw.usmsocial.repositories.CommentRepository;
import me.yattaw.usmsocial.repositories.LikeRepository;
import me.yattaw.usmsocial.repositories.PostRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.requests.UserPostRequest;
import me.yattaw.usmsocial.user.requests.UserRequest;
import me.yattaw.usmsocial.user.responses.PostCommentResponse;
import me.yattaw.usmsocial.user.responses.RecommendedPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    private final JwtService jwtService;

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

    public ResponseEntity<Page<RecommendedPostResponse>> getRecommendedPosts() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserPost> posts = postRepository.findAll(pageRequest);
        return ResponseEntity.ok(posts.map(this::mapToSimplifiedPostResponse));
    }

    private RecommendedPostResponse mapToSimplifiedPostResponse(UserPost post) {

        return RecommendedPostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .postersFirstName(post.getUser().getFirstName())
                .postersLastName(post.getUser().getLastName())
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
                .timestamp(post.getTimestamp())
                .likeCount(postRepository.getLikeCount(post.getId())) // Find a better way of doing this if possible
                .build();
    }

    public UserActionResponse likePost(
            HttpServletRequest servletRequest,
            UserRequest request
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
                return UserActionResponse.builder()
                        .status(0)
                        .message("User has already liked this post.")
                        .build();
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
