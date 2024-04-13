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

public interface PostRepository extends JpaRepository<UserPost, Integer> {

    @Transactional
    void deleteByUser(User user);

    Page<UserPost> findAll(Pageable pageable);
    
    @Query(
        value = "SELECT * FROM usm_social_posts WHERE timestamp <= :datetime", 
        nativeQuery = true)
    Page<UserPost> getRecommended(@Param("datetime") LocalDateTime datetime, Pageable pageable);

    @Query(
        value = "SELECT COUNT(*) FROM usm_social_posts WHERE timestamp <= :datetimeCurrent AND timestamp >= :datetimeFetchBefore ORDER BY timestamp DESC",
        nativeQuery = true)
    Integer getNewRecommendedPostsCount(@Param("datetimeCurrent") LocalDateTime datetimeCurrent, @Param("datetimeFetchBefore") LocalDateTime datetimeFetchBefore);

    @Query(
        value = "SELECT * FROM usm_social_posts WHERE timestamp <= :datetimeCurrent AND timestamp >= :datetimeFetchBefore ORDER BY timestamp DESC", 
        nativeQuery = true)
    List<UserPost> getNewRecommendedPosts(@Param("datetimeCurrent") LocalDateTime datetimeCurrent, @Param("datetimeFetchBefore") LocalDateTime datetimeFetchBefore);

    @Query("SELECT COUNT(pl) FROM UserPost up JOIN up.likes pl WHERE up.id = :postId")
    Integer getLikeCount(@Param("postId") Integer postId);

    @Query(
        value = "SELECT COUNT(*) FROM usm_social_posts WHERE user_id = :userId AND timestamp <= :datetimeCurrent AND timestamp >= :datetimeFetchBefore ORDER BY timestamp DESC",
        nativeQuery = true)
    Integer getNewUserPostsCount(@Param("userId") Integer userId, @Param("datetimeCurrent") LocalDateTime datetimeCurrent, @Param("datetimeFetchBefore") LocalDateTime datetimeFetchBefore);

    @Query(
        value = "SELECT * FROM usm_social_posts WHERE user_id = :userId AND timestamp <= :datetimeCurrent AND timestamp >= :datetimeFetchBefore ORDER BY timestamp DESC", 
        nativeQuery = true)
    List<UserPost> getNewUserPosts(@Param("userId") Integer userId, @Param("datetimeCurrent") LocalDateTime datetimeCurrent, @Param("datetimeFetchBefore") LocalDateTime datetimeFetchBefore);

    @Query(
        value = "SELECT * FROM usm_social_posts WHERE user_id = :userId AND timestamp <= :datetime", 
        nativeQuery = true)
    Page<UserPost> getUserPosts(@Param("userId") Integer userId, @Param("datetime") LocalDateTime datetime, Pageable pageable);

    @Query(
        value = "SELECT COUNT(*) FROM usm_social_posts WHERE user_id = :userId",
        nativeQuery = true)
    Integer getUserPostCount(@Param("userId") Integer userId);
}
