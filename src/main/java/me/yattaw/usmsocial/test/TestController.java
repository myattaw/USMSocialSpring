package me.yattaw.usmsocial.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for testing purposes.
 *
 * <p>
 * This controller provides a simple endpoint for testing purposes.
 * </p>
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    /**
     * Handles GET requests to test the endpoint.
     *
     * @return A ResponseEntity with a string "Test Response" as the response body.
     */
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test Response");
    }

}
