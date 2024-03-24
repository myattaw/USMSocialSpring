package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    @Query(
        value = "SELECT * FROM usm_social_users WHERE LOCATE(:queryItem, LOWER(first_name)) > 0 OR LOCATE(:queryItem, LOWER(last_name)) > 0 OR LOCATE(:queryItem, LOWER(CONCAT(first_name, last_name))) > 0 ORDER BY first_name ASC, last_name ASC",
        nativeQuery = true)
    Page<User> getUserSearchName(@Param("queryItem") String queryItem, Pageable pageable);
}