package me.yattaw.usmsocial.admin;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.admin.reponse.UserReportResponse;
import me.yattaw.usmsocial.entities.post.PostComment;
import me.yattaw.usmsocial.entities.report.ReportType;
import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import me.yattaw.usmsocial.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
/**
 * Service class providing admin-related functionalities.
 *
 * <p>
 * This service class encapsulates operations related to admin actions, such as user and post deletion, and retrieving reports.
 * </p>
 *
 * @version 17 April 2024
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    /**
     * Deletes a user and related posts.
     *
     * @param request The {@link AdminDeleteRequest} containing the ID of the user to be deleted.
     * @return An {@link AdminActionResponse} indicating the status and outcome of the deletion operation.
     */
    public AdminActionResponse deleteUser(AdminDeleteRequest request) {
        Optional<User> user = userRepository.findById(request.getTargetId());

        if (user.isPresent()) {
            // Delete or update related posts
            postRepository.deleteByUser(user.get());

            // Now delete the user
            userRepository.delete(user.get());

            return AdminActionResponse.builder()
                    .status(1)
                    .message("User and related posts have been successfully deleted.")
                    .build();
        } else {
            return AdminActionResponse.builder()
                    .status(0)
                    .message("User could not be found.")
                    .build();
        }
    }

    /**
     * Deletes a post and related comments and likes.
     *
     * @param request The {@link AdminDeleteRequest} containing the ID of the post to be deleted.
     * @return An {@link AdminActionResponse} indicating the status and outcome of the deletion operation.
     */
    public AdminActionResponse deletePost(AdminDeleteRequest request) {
        Optional<UserPost> userPost = postRepository.findById(request.getTargetId());

        if (userPost.isPresent()) {
            // Delete posts and all likes and comments attached to post
            commentRepository.deleteAll(userPost.get().getComments());
            likeRepository.deleteAll(userPost.get().getLikes());
            postRepository.delete(userPost.get());

            return AdminActionResponse.builder()
                    .status(1)
                    .message("Post has been successfully deleted.")
                    .build();
        } else {
            return AdminActionResponse.builder()
                    .status(0)
                    .message("Post could not be found.")
                    .build();
        }
    }

    /**
     * Retrieves reports based on the specified report type.
     *
     * @param reportType The {@link ReportType} indicating the type of reports to retrieve.
     * @return A list of {@link UserReportResponse} objects containing information about the reported entities.
     */
    public List<UserReportResponse> getReports(ReportType reportType) {
        List<UserReport> reports = reportRepository.findAllReports(reportType);
        if (!reports.isEmpty()) {
            List<UserReportResponse> userReportResponses = new ArrayList<>(reports.size());
            reports.forEach(report -> {
                switch (reportType) {
                    case REPORTED_USER -> {
                        Optional<User> user = userRepository.findById(report.getReported());
                        user.ifPresent(value -> userReportResponses.add(UserReportResponse.builder()
                                .firstName(value.getFirstName())
                                .lastName(value.getLastName())
                                .reportReason(report.getReason())
                                .reportType(report.getReportType())
                                .timestamp(report.getTimestamp())
                                .build()
                        ));
                    }
                    case REPORTED_POST -> {
                        Optional<UserPost> post = postRepository.findById(report.getReported());
                        post.ifPresent(value -> userReportResponses.add(UserReportResponse.builder()
                                .firstName(value.getUser().getFirstName())
                                .lastName(value.getUser().getLastName())
                                .reportReason(report.getReason())
                                .reportType(report.getReportType())
                                .timestamp(report.getTimestamp())
                                .build()
                        ));
                    }
                    case REPORTED_COMMENT -> {
                        Optional<PostComment> comment = commentRepository.findById(report.getReported());
                        comment.ifPresent(value -> userReportResponses.add(UserReportResponse.builder()
                                .firstName(value.getUser().getFirstName())
                                .lastName(value.getUser().getLastName())
                                .reportReason(report.getReason())
                                .reportType(report.getReportType())
                                .timestamp(report.getTimestamp())
                                .build()
                        ));
                    }
                }
            });
            return userReportResponses;
        }
        return Collections.emptyList();
    }

}