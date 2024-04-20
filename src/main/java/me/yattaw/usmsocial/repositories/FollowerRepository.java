package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.user.UserFollowerId;
import me.yattaw.usmsocial.entities.user.UserFollowers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 * Repository interface for managing follower relationships in the database.
 */
public interface FollowerRepository extends JpaRepository<UserFollowers, UserFollowerId> {

    /**
     * Retrieves the count of users who follow each other.
     *
     * @param followerId  The ID of the follower.
     * @param followingId The ID of the user being followed.
     * @return The count of users who follow each other.
     */
    @Query(
            value = "SELECT COUNT(*) FROM usm_social_followers WHERE following_id = :followingId AND follower_id = :followerId AND follower_id != following_id",
            nativeQuery = true
    )
    Integer getUserFollowerEachOtherCount(@Param("followerId") Integer followerId, @Param("followingId") Integer followingId);

    /**
     * Retrieves the IDs of users who follow a specific user.
     *
     * @param followerId The ID of the user being followed.
     * @param pageable   Pageable object for pagination.
     * @return Page containing the IDs of users who follow the specified user.
     */
    @Query(
        value = "SELECT follower_id FROM usm_social_followers WHERE following_id = :followerId",
        nativeQuery = true
    )
    Page<Integer> getUserFollowers(@Param("followerId") Integer followerId, Pageable pageable);

    /**
     * Retrieves the IDs of users followed by a specific user.
     *
     * @param followingId The ID of the user following others.
     * @param pageable    Pageable object for pagination.
     * @return Page containing the IDs of users followed by the specified user.
     */
    @Query(
        value = "SELECT following_id FROM usm_social_followers WHERE follower_id = :followingId",
        nativeQuery = true
    )
    Page<Integer> getUserFollowings(@Param("followingId") Integer followingId, Pageable pageable);

    /**
     * Retrieves the count of users followed by a specific user.
     *
     * @param followingId The ID of the user following others.
     * @return The count of users followed by the specified user.
     */
    @Query(
        value = "SELECT COUNT(*) FROM usm_social_followers WHERE follower_id = :followingId",
        nativeQuery = true
    )
    Integer getCountFollowings(@Param("followingId") Integer followingId);

    /**
     * Retrieves the count of users following a specific user.
     *
     * @param followerId The ID of the user being followed.
     * @return The count of users following the specified user.
     */
    @Query(
        value = "SELECT COUNT(*) FROM usm_social_followers WHERE following_id = :followerId",
        nativeQuery = true
    )
    Integer getCountFollowers(@Param("followerId") Integer followerId);
}