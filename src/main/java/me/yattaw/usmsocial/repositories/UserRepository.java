package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for managing users in the database.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user.
     * @return An optional containing the user, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their verification token.
     *
     * @param token The verification token.
     * @return An optional containing the user, or empty if not found.
     */
    Optional<User> findByVerificationToken(String token);

    /**
     * Retrieves users based on a search query.
     *
     * @param queryItem The search query.
     * @param pageable  Pagination information.
     * @return A page of users matching the search query.
     */
    @Query(
            value = "SELECT * FROM usm_social_users WHERE LOCATE(:queryItem, LOWER(first_name)) > 0 OR LOCATE(:queryItem, LOWER(last_name)) > 0 OR LOCATE(:queryItem, LOWER(CONCAT(first_name, last_name))) > 0 ORDER BY first_name ASC, last_name ASC",
            nativeQuery = true)
    Page<User> getUserSearchName(@Param("queryItem") String queryItem, Pageable pageable);
}