package me.yattaw.usmsocial.repositories;

import jakarta.transaction.Transactional;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<UserPost, Integer> {

    @Transactional
    void deleteByUser(User user);

    Page<UserPost> findAll(Pageable pageable);

    @Query("SELECT COUNT(pl) FROM UserPost up JOIN up.likes pl WHERE up.id = :postId")
    Integer getLikeCount(@Param("postId") Integer postId);

}
