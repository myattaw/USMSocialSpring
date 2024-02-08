package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<UserPost, Integer> {

}
