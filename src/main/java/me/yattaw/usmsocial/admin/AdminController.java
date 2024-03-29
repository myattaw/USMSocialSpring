package me.yattaw.usmsocial.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/reports/{reportType}")
    public ResponseEntity<String> viewReports(
            @PathVariable String reportType
    ) {
        return ResponseEntity.ok("not implemented yet");
    }


}
