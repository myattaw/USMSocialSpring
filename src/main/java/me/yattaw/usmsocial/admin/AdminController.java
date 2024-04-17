package me.yattaw.usmsocial.admin;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.admin.reponse.UserReportResponse;
import me.yattaw.usmsocial.entities.report.ReportType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller class to handle admin-related endpoints.
 *
 * <p>
 * This controller provides endpoints for performing administrative actions such as deleting users and posts,
 * as well as viewing reports on users, comments, and posts.
 * </p>
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    /**
     * Deletes a user based on the provided request.
     *
     * @param request The request containing information about the user to delete.
     * @return ResponseEntity containing the response status and message.
     */
    @DeleteMapping("/delete/user")
    public ResponseEntity<AdminActionResponse> deleteUser(
            @RequestBody AdminDeleteRequest request
    ) {
        return ResponseEntity.ok(service.deleteUser(request));
    }

    /**
     * Deletes a post based on the provided request.
     *
     * @param request The request containing information about the post to delete.
     * @return ResponseEntity containing the response status and message.
     */
    @DeleteMapping("/delete/post")
    public ResponseEntity<AdminActionResponse> deletePost(
            @RequestBody AdminDeleteRequest request
    ) {
        return ResponseEntity.ok(service.deletePost(request));
    }

    /**
     * Retrieves all user reports.
     *
     * @return ResponseEntity containing a list of user report responses.
     */
    @GetMapping("/reports/users/")
    public ResponseEntity<List<UserReportResponse>> viewAllUserReports() {
        return ResponseEntity.ok(service.getReports(ReportType.REPORTED_USER));
    }

    /**
     * Retrieves all comment reports.
     *
     * @return ResponseEntity containing a list of user report responses.
     */
    @GetMapping("/reports/comments/")
    public ResponseEntity<List<UserReportResponse>> viewAllCommentReports() {
        return ResponseEntity.ok(service.getReports(ReportType.REPORTED_COMMENT));
    }

    /**
     * Retrieves all post reports.
     *
     * @return ResponseEntity containing a list of user report responses.
     */
    @GetMapping("/reports/posts/")
    public ResponseEntity<List<UserReportResponse>> viewAllPostReports() {
        return ResponseEntity.ok(service.getReports(ReportType.REPORTED_POST));
    }

}
