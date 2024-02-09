package me.yattaw.usmsocial.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @DeleteMapping("/delete_user")
    public ResponseEntity<AdminActionResponse> deleteUser(
            @RequestBody AdminDeleteRequest request
    ) {
        return ResponseEntity.ok(service.deleteUser(request));
    }

    @DeleteMapping("/delete_post")
    public ResponseEntity<AdminActionResponse> deletePost(
            @RequestBody AdminDeleteRequest request
    ) {
        return ResponseEntity.ok(service.deletePost(request));
    }

}
