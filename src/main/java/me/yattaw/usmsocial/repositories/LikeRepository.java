package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<PostLike, Integer> {


    @Query(
        value = "SELECT COUNT(*) FROM usm_social_post_likes WHERE user_id = :userId AND post_id = :postId",
        nativeQuery = true)
    Integer getCountOfUserAndPostLikes(@Param("userId") Integer userId, @Param("postId") Integer postId);
}
