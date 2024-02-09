package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.post.PostComment;
import me.yattaw.usmsocial.entities.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<PostComment, Integer> {

}
