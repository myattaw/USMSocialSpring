package me.yattaw.usmsocial.user;

import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.auth.RegisterRequest;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.service.EmailSenderService;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
/**
 * Controller class that handles account verification and password reset/change requests.
 *
 * @version 17 April 2024
 */
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class VerificationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService senderService;

    /**
     * Verifies a user's account based on the verification token.
     *
     * @param token The verification token.
     * @return ResponseEntity containing the verification status message.
     */
    @GetMapping("verify/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        // Find user by verification token
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Mark user account as verified
            user.setRole(Role.STUDENT);
            user.setVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return ResponseEntity.ok("Account verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token.");
        }
    }


    // make reset_password and change_password
    /**
     * Initiates the password reset process by sending a reset password email.
     *
     * @param request The reset password request containing the user's email.
     * @return ResponseEntity indicating the status of the password reset request.
     */
    @PostMapping("reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody RegisterRequest request) {
        Optional<User> emailUser = userRepository.findByEmail(request.getEmail());
        if (emailUser.isPresent()) {
            User user = emailUser.get();
            //TODO: Also check if the token verification expired
            if (user.getVerificationToken() == null) {
                user.generateVerificationToken();
                userRepository.save(user);
                senderService.sendEmail(
                        user,
                        "Password Change Confirmation for USM Social Account",
                        "Thank you for using USM Social! To complete the password change process, " +
                                "please click the link below:",
                        "https://www.mainecollege.tech/#/passwordchange/" + user.getEmail().split("@")[0] + "/" + user.getVerificationToken(),
                        "Change Password"
                );
            }
        }
        return ResponseEntity.ok("Sent a password reset email if there is a user associated with the email address.");
    }

    /**
     * Changes the user's password based on the provided token.
     *
     * @param token   The password change token.
     * @param request The password change request containing the new password and email.
     * @return ResponseEntity containing the password change status message.
     */
    @PostMapping("change_password/{token}")
    public ResponseEntity<String> changePassword(@PathVariable String token, @RequestBody RegisterRequest request) {
        // Find user by verification token
        Optional<User> tokenUser = userRepository.findByVerificationToken(token);
        Optional<User> emailUser = userRepository.findByEmail(request.getEmail());

        if (tokenUser.isPresent() && emailUser.isPresent() && emailUser.get().equals(tokenUser.get())) {
            User user = emailUser.get();
            user.setVerificationToken(null);
            user.setVerified(true); // Just verify anyway because they clicked the URL from the email.
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("Account password successfully changed.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password token.");
        }
    }

}