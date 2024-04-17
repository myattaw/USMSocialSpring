package me.yattaw.usmsocial.repositories;

import jakarta.transaction.Transactional;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing posts in the database.
 */
public interface PostRepository extends JpaRepository<UserPost, Integer> {

    /**
     * Deletes all posts associated with a specific user.
     *
     * @param user The user whose posts are to be deleted.
     */
    @Transactional
    void deleteByUser(User user);

    /**
     * Retrieves all posts.
     *
     * @param pageable Pagination information.
     * @return A page of user posts.
     */
    Page<UserPost> findAll(Pageable pageable);

    /**
     * Retrieves recommended posts based on a datetime.
     *
     * @param datetime The datetime.
     * @param pageable Pagination information.
     * @return A page of recommended user posts.
     */
    @Query(
            value = "SELECT * FROM usm_social_posts WHERE timestamp <= :datetime",
            nativeQuery = true)
    Page<UserPost> getRecommended(@Param("datetime") LocalDateTime datetime, Pageable pageable);

    /**
     * Retrieves the count of likes for a specific post.
     *
     * @param postId The ID of the post.
     * @return The count of likes for the post.
     */
    @Query("SELECT COUNT(pl) FROM UserPost up JOIN up.likes pl WHERE up.id = :postId")
    Integer getLikeCount(@Param("postId") Integer postId);

    /**
     * Retrieves the count of new posts for a specific user within a given time range.
     *
     * @param userId The ID of the user.
     * @param datetimeCurrent The current datetime.
     * @param datetimeFetchBefore The datetime to fetch posts before.
     * @return The count of new posts for the user.
     */
    @Query(
            value = "SELECT COUNT(*) FROM usm_social_posts WHERE user_id = :userId AND timestamp <= :datetimeCurrent AND timestamp >= :datetimeFetchBefore ORDER BY timestamp DESC",
            nativeQuery = true)
    Integer getNewUserPostsCount(@Param("userId") Integer userId, @Param("datetimeCurrent") LocalDateTime datetimeCurrent, @Param("datetimeFetchBefore") LocalDateTime datetimeFetchBefore);

    /**
     * Retrieves new posts for a specific user within a given time range.
     *
     * @param userId The ID of the user.
     * @param datetimeCurrent The current datetime.
     * @param datetimeFetchBefore The datetime to fetch posts before.
     * @return A list of new posts for the user.
     */
    @Query(
            value = "SELECT * FROM usm_social_posts WHERE user_id = :userId AND timestamp <= :datetimeCurrent AND timestamp >= :datetimeFetchBefore ORDER BY timestamp DESC",
            nativeQuery = true)
    List<UserPost> getNewUserPosts(@Param("userId") Integer userId, @Param("datetimeCurrent") LocalDateTime datetimeCurrent, @Param("datetimeFetchBefore") LocalDateTime datetimeFetchBefore);

    /**
     * Retrieves the count of posts for a specific user.
     *
     * @param userId The ID of the user.
     * @return The count of posts for the user.
     */
    @Query(
            value = "SELECT COUNT(*) FROM usm_social_posts WHERE user_id = :userId",
            nativeQuery = true)
    Integer getUserPostCount(@Param("userId") Integer userId);
}
