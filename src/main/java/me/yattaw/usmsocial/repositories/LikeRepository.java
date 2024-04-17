package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing likes on posts in the database.
 */
public interface LikeRepository extends JpaRepository<PostLike, Integer> {

    /**
     * Retrieves the count of likes for a specific user and post.
     *
     * @param userId The ID of the user.
     * @param postId The ID of the post.
     * @return The count of likes for the specified user and post.
     */
    @Query(
            value = "SELECT COUNT(*) FROM usm_social_post_likes WHERE user_id = :userId AND post_id = :postId",
            nativeQuery = true)
    Integer getCountOfUserAndPostLikes(@Param("userId") Integer userId, @Param("postId") Integer postId);
}
