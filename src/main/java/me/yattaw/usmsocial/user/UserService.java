package me.yattaw.usmsocial.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.report.UserReportRequest;
import me.yattaw.usmsocial.entities.user.Role;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserFollowerId;
import me.yattaw.usmsocial.entities.user.UserFollowers;
import me.yattaw.usmsocial.entities.user.UserInfo;
import me.yattaw.usmsocial.repositories.FollowerRepository;
import me.yattaw.usmsocial.repositories.ReportRepository;
import me.yattaw.usmsocial.repositories.UserRepository;
import me.yattaw.usmsocial.service.JwtService;
import me.yattaw.usmsocial.user.responses.AuthenicationException;
import me.yattaw.usmsocial.user.responses.UserActionResponse;
import me.yattaw.usmsocial.user.responses.UserFollowListResponse;
import me.yattaw.usmsocial.user.responses.UserFollowerCountResponse;
import me.yattaw.usmsocial.user.responses.UserFollowingCountResponse;
import me.yattaw.usmsocial.user.responses.UserInfoResponse;
import me.yattaw.usmsocial.user.responses.UserProfilePicture;
import me.yattaw.usmsocial.user.responses.UserSearch;
import me.yattaw.usmsocial.user.responses.UserSearchResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.AuthenticationException;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class providing various operations related to user management.
 *
 * @version 17 April 2024
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final FollowerRepository followerRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Retrieves the current user based on the JWT token extracted from the servlet request.
     *
     * @param servletRequest The HTTP servlet request.
     * @return Optional containing the current user, if found.
     */
    private Optional<User> getCurrentUser(HttpServletRequest servletRequest) {
        String token = jwtService.extractToken(servletRequest);
        return userRepository.findByEmail(jwtService.fetchEmail(token));
    }

    /**
     * Checks if the current user has authorized access.
     *
     * @param servletRequest The HTTP servlet request.
     * @throws AuthenticationException if the user is not authorized.
     */
    private void isAuthorizedAccess(HttpServletRequest servletRequest) throws AuthenticationException {
        Optional<User> userRequesting = getCurrentUser(servletRequest);

        if (userRequesting.isEmpty() || userRequesting.get().getRole() == Role.GUEST) {
            throw new AuthenicationException("Only users can access");
        }
    }

    /**
     * Retrieves the information of a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param userId         The ID of the user to retrieve information for.
     * @return UserInfoResponse containing the user information.
     */
    public UserInfoResponse getUserInfo(HttpServletRequest servletRequest, Integer userId) {
        Optional<User> userFetching = getCurrentUser(servletRequest);

        Optional<User> userProfile = userRepository.findById(userId);

        int count = followerRepository.getUserFollowerEachOtherCount(userFetching.get().getId(), userProfile.get().getId());
        System.out.println(count);
        boolean isFollowing = count > 0;
        boolean isOwnProfile = userProfile.get().getId() == userFetching.get().getId();
        
        return UserInfoResponse.builder()
                .user(userProfile.get().getUserInfo())
                .isFollowing(isFollowing)
                .isOwnProfile(isOwnProfile)
                .build();
    }

    /**
     * Retrieves the profile information of the current user.
     *
     * @param servletRequest The HTTP servlet request.
     * @return UserInfoResponse containing the profile information.
     */
    public UserInfoResponse getProfileUserInfo(HttpServletRequest servletRequest) {
        Optional<User> user = getCurrentUser(servletRequest);

        return UserInfoResponse.builder().user(user.get().getUserInfo()).build();
    }

    /**
     * Retrieves the profile picture of the current user.
     *
     * @param servletRequest The HTTP servlet request.
     * @return UserProfilePicture containing the profile picture.
     */
    public UserProfilePicture getProfilePicture(HttpServletRequest servletRequest) {
        Optional<User> user = getCurrentUser(servletRequest);

        return UserProfilePicture.builder().imageBase64((user.isEmpty()) ? "" : user.get().getBase64ProfilePicture()).build();
    }

    /**
     * Sets the profile information of the current user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param userInfo       The updated user information.
     * @return UserActionResponse indicating the status of the operation.
     */
    public UserActionResponse setProfileUserInfo(HttpServletRequest servletRequest, UserInfo userInfo) {
        Optional<User> user = getCurrentUser(servletRequest);

        user.get().setFirstName(userInfo.getFirstName());
        user.get().setLastName(userInfo.getLastName());
        user.get().setEmail(userInfo.getEmail());
        user.get().setTagLine(userInfo.getTagLine());
        user.get().setBio(userInfo.getBio());
        userRepository.save(user.get());

        return UserActionResponse.builder()
                .status(1)
                .message("User has successfully updated their profile!")
                .build();
    }

    /**
     * Searches for users based on the provided query.
     *
     * @param servletRequest The HTTP servlet request.
     * @param searchQuery    The search query.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @return UserSearchResponse containing the search results.
     */
    public UserSearchResponse getUserSearch(HttpServletRequest servletRequest, 
            String searchQuery, Integer pageNumber, Integer pageSize) {
        isAuthorizedAccess(servletRequest);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        searchQuery = searchQuery.replaceAll(" ", "");
        searchQuery = searchQuery.replaceAll("\\.", "");
        searchQuery = searchQuery.toLowerCase();

        Page<User> usersResult = userRepository.getUserSearchName(searchQuery, pageRequest);
        Page<UserSearch> users = usersResult.map(this::mapToSearchUserResponse);

        return UserSearchResponse.builder().users(users).build();
    }

    /**
     * Handles follow and unfollow actions for a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param followingId    The ID of the user being followed/unfollowed.
     * @param isFollow       Indicates whether the action is follow (true) or unfollow (false).
     * @return UserActionResponse indicating the status of the action.
     */
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

    /**
     * Maps a User entity to a UserSearch object.
     *
     * @param user The User entity to map.
     * @return UserSearch object containing mapped data.
     */
    private UserSearch mapToSearchUserResponse(User user) {
        return UserSearch.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .base64Image(user.getBase64ProfilePicture())
                .build();
    }

    /**
     * Maps a user ID to a UserSearch object.
     *
     * @param userId The user ID to map.
     * @return UserSearch object containing mapped data.
     */
    private UserSearch mapToSearchUserResponse(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        return (user.isEmpty()) ?
             UserSearch.builder().base64Image("").firstName("Deleted").lastName("").build() :
             UserSearch.builder().firstName(user.get().getFirstName()).lastName(user.get().getLastName()).id(user.get().getId()).base64Image(user.get().getBase64ProfilePicture()).build();
    }

    /**
     * Initiates the follow action for a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param followingId    The ID of the user to follow.
     * @return UserActionResponse indicating the status of the follow action.
     */
    public UserActionResponse followUser(HttpServletRequest servletRequest, Integer followingId) {
        return handleUserAction(servletRequest, followingId, true);
    }

    /**
     * Initiates the unfollow action for a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param followingId    The ID of the user to unfollow.
     * @return UserActionResponse indicating the status of the unfollow action.
     */
    public UserActionResponse unfollowUser(HttpServletRequest servletRequest, Integer followingId) {
        return handleUserAction(servletRequest, followingId, false);
    }

    /**
     * Uploads a profile picture for the current user.
     *
     * @param request   The HTTP request.
     * @param imageData The profile picture image data.
     * @return UserActionResponse indicating the status of the profile picture upload.
     */
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

    /**
     * Retrieves the count of followers for a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param userId         The ID of the user to retrieve follower count for.
     * @return UserFollowerCountResponse containing the follower count.
     */
    public UserFollowerCountResponse getFollowersCount(HttpServletRequest servletRequest, int userId) {
        isAuthorizedAccess(servletRequest);
        int count = followerRepository.getCountFollowers(userId);

        return UserFollowerCountResponse.builder().followerCount(count).build();
    }

    /**
     * Retrieves the count of users followed by a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param userId         The ID of the user to retrieve following count for.
     * @return UserFollowingCountResponse containing the following count.
     */
    public UserFollowingCountResponse getFollowingsCount(HttpServletRequest servletRequest, int userId) {
        isAuthorizedAccess(servletRequest);
        int count = followerRepository.getCountFollowings(userId);

        return UserFollowingCountResponse.builder().followingCount(count).build();
    }

    /**
     * Retrieves the list of followers for a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param userId         The ID of the user to retrieve followers for.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @return UserFollowListResponse containing the list of followers.
     */
    public UserFollowListResponse getFollowers(HttpServletRequest servletRequest, int userId, int pageNumber, int pageSize) {
        isAuthorizedAccess(servletRequest);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Integer> usersResult = followerRepository.getUserFollowers(userId, pageRequest);
        Page<UserSearch> userSearched =  usersResult.map(this::mapToSearchUserResponse);

        return UserFollowListResponse.builder().userFollowList(userSearched).build();
    }

    /**
     * Retrieves the list of users followed by a user.
     *
     * @param servletRequest The HTTP servlet request.
     * @param userId         The ID of the user to retrieve followed users for.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @return UserFollowListResponse containing the list of followed users.
     */
    public UserFollowListResponse getFollowings(HttpServletRequest servletRequest, int userId, int pageNumber, int pageSize) {
        isAuthorizedAccess(servletRequest);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Integer> usersResult = followerRepository.getUserFollowings(userId, pageRequest);
        Page<UserSearch> userSearched =  usersResult.map(this::mapToSearchUserResponse);

        return UserFollowListResponse.builder().userFollowList(userSearched).build();
    }

    /**
     * Reports a user.
     *
     * @param request      The HTTP request.
     * @param reportRequest The user report request.
     * @return UserActionResponse indicating the status of the report action.
     */
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

    /**
     * Checks if the provided image data represents a valid image format.
     *
     * @param imageData The image data.
     * @return True if the image format is valid, otherwise false.
     */
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
