package me.yattaw.usmsocial.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.report.UserReportRequest;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserFollowerId;
import me.yattaw.usmsocial.entities.user.UserFollowers;
import me.yattaw.usmsocial.repositories.FollowerRepository;
import me.yattaw.usmsocial.repositories.ReportRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FollowerRepository followerRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private Optional<User> getCurrentUser(HttpServletRequest servletRequest) {
        String token = jwtService.extractToken(servletRequest);
        return userRepository.findByEmail(jwtService.fetchEmail(token));
    }

    private UserActionResponse handleUserAction(HttpServletRequest servletRequest, Integer followingId, boolean isFollow) {
        Optional<User> user = getCurrentUser(servletRequest);

        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        if (isFollow) {
            followerRepository.save(UserFollowers.builder()
                    .id(UserFollowerId.builder()
                            .following(userRepository.getById(followingId))
                            .follower(user.get())
                            .build())
                    .timestamp(LocalDateTime.now())
                    .build());
        } else {
            followerRepository.deleteById(UserFollowerId.builder()
                    .following(userRepository.getById(followingId))
                    .follower(user.get())
                    .build());
        }

        String actionMessage = isFollow ? "followed" : "unfollowed";
        return UserActionResponse.builder()
                .status(1)
                .message(String.format("User has successfully %s!", actionMessage))
                .build();
    }

    public UserActionResponse followUser(HttpServletRequest servletRequest, Integer followingId) {
        return handleUserAction(servletRequest, followingId, true);
    }

    public UserActionResponse unfollowUser(HttpServletRequest servletRequest, Integer followingId) {
        return handleUserAction(servletRequest, followingId, false);
    }

    public UserActionResponse uploadProfilePicture(HttpServletRequest request, byte[] imageData) {
        Optional<User> user = getCurrentUser(request);

        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        int maxSizeInBytes = 10 * 1024 * 1024;
        if (imageData.length > maxSizeInBytes) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Image size exceeds the maximum allowed limit.")
                    .build();
        }

        if (!isValidImageFormat(imageData)) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Invalid image format.")
                    .build();
        }

        user.get().setProfilePicture(imageData);
        userRepository.save(user.get());

        return UserActionResponse.builder()
                .status(1)
                .message("User has successfully updated profile picture!")
                .build();
    }

    public UserActionResponse reportId(HttpServletRequest request, UserReportRequest reportRequest) {
        Optional<User> user = getCurrentUser(request);

        if (user.isEmpty()) {
            return UserActionResponse.builder()
                    .status(0)
                    .message("Unable to authorize the user token.")
                    .build();
        }

        UserReport userReport = UserReport.builder()
                .reported(reportRequest.getTargetId())
                .reason(reportRequest.getReason())
                .reportType(reportRequest.getReportType())
                .timestamp(LocalDateTime.now())
                .build();

        reportRepository.save(userReport);

        return UserActionResponse.builder()
                .status(1)
                .message(String.format("You have successfully reported %d for %s.",
                        reportRequest.getTargetId(),
                        reportRequest.getReportType().name())
                ).build();
    }

    private boolean isValidImageFormat(byte[] imageData) {
        try (InputStream inputStream = new ByteArrayInputStream(imageData)) {
            ImageIO.read(inputStream);
            return true;  // If successful, the format is valid
        } catch (IOException e) {
            // If an IOException occurs, it means the image format is not valid
            return false;
        }
    }

}
