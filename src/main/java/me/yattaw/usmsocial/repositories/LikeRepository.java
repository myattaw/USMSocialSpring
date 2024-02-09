package me.yattaw.usmsocial.repositories;

import jakarta.transaction.Transactional;
import me.yattaw.usmsocial.entities.post.PostLike;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<PostLike, Integer> {

}
