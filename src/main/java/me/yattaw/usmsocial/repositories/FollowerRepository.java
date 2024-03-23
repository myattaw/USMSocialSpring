package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.user.UserFollowerId;
import me.yattaw.usmsocial.entities.user.UserFollowers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository extends JpaRepository<UserFollowers, UserFollowerId> {

    @Query(
        value = "SELECT follower_id, following_id FROM usm_social_followers WHERE following_id = :followingId AND follower_id = :followerId",
        nativeQuery = true)
    List<UserFollowerId> getUserFollowerEachOther(@Param("followerId") Integer followerId, @Param("followingId") Integer followingId);
    
    @Query(
        value = "SELECT follower_id, following_id FROM usm_social_followers WHERE follower_id = :followerId",
        nativeQuery = true)
    List<UserFollowerId> getUserFollowers(@Param("followerId") Integer followerId);

    @Query(
        value = "SELECT follower_id, following_id FROM usm_social_followers WHERE following_id = :followingId",
        nativeQuery = true)
    List<UserFollowerId> getUserFollowings(@Param("followingId") Integer followingId);

}
