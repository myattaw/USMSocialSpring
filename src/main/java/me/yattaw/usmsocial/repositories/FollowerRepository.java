package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.user.UserFollowerId;
import me.yattaw.usmsocial.entities.user.UserFollowers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository extends JpaRepository<UserFollowers, UserFollowerId> {

    @Query(
        value = "SELECT COUNT(*) FROM usm_social_followers WHERE following_id = :followingId AND follower_id = :followerId AND follower_id != following_id",
        nativeQuery = true)
    Integer getUserFollowerEachOtherCount(@Param("followerId") Integer followerId, @Param("followingId") Integer followingId);

    @Query(
        value = "SELECT following_id FROM usm_social_followers WHERE follower_id = :followerId",
        nativeQuery = true)
    Page<Integer> getUserFollowers(@Param("followerId") Integer followerId, Pageable pageable);

    @Query(
        value = "SELECT follower_id FROM usm_social_followers WHERE following_id = :followingId",
        nativeQuery = true)
    Page<Integer> getUserFollowings(@Param("followingId") Integer followingId, Pageable pageable);

    @Query(
        value = "SELECT COUNT(*) FROM usm_social_followers WHERE following_id = :followingId",
        nativeQuery = true)
    Integer getCountFollowings(@Param("followingId") Integer followingId);

    @Query(
        value = "SELECT COUNT(*) FROM usm_social_followers WHERE follower_id = :followerId",
        nativeQuery = true)
    Integer getCountFollowers(@Param("followerId") Integer followerId);
}
