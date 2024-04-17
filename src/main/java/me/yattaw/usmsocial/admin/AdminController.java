package me.yattaw.usmsocial.admin;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.admin.reponse.UserReportResponse;
import me.yattaw.usmsocial.entities.report.ReportType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @DeleteMapping("/delete/user")
    public ResponseEntity<AdminActionResponse> deleteUser(
            @RequestBody AdminDeleteRequest request
    ) {
        return ResponseEntity.ok(service.deleteUser(request));
    }

    @DeleteMapping("/delete/post")
    public ResponseEntity<AdminActionResponse> deletePost(
            @RequestBody AdminDeleteRequest request
    ) {
        return ResponseEntity.ok(service.deletePost(request));
    }

    @GetMapping("/reports/users/")
    public ResponseEntity<List<UserReportResponse>> viewAllUserReports() {
        return ResponseEntity.ok(service.getReports(ReportType.REPORTED_USER));
    }

    @GetMapping("/reports/comments/")
    public ResponseEntity<List<UserReportResponse>> viewAllCommentReports() {
        return ResponseEntity.ok(service.getReports(ReportType.REPORTED_COMMENT));
    }

    @GetMapping("/reports/posts/")
    public ResponseEntity<List<UserReportResponse>> viewAllPostReports() {
        return ResponseEntity.ok(service.getReports(ReportType.REPORTED_POST));
    }

}
