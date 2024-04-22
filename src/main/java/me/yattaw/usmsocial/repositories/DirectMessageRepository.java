package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.message.DirectMessage;
import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Repository interface for managing direct messages in the database.
 */
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {

    /**
     * Retrieves a list of users who have sent messages from a specific user.
     *
     * @param user The user whose messages are being queried.
     * @return A list of users who have sent messages from the specified user.
     */
    @Query("SELECT dm.receiver FROM DirectMessage dm WHERE dm.sender = :user")
    List<User> findUsersWithMessagesFromUser(User user);

    /**
     * Retrieves a list of users who have received messages from a specific user.
     *
     * @param user The user whose messages are being queried.
     * @return A list of users who have received messages from the specified user.
     */
    @Query("SELECT dm.sender FROM DirectMessage dm WHERE dm.receiver = :user")
    List<User> findUsersWithMessagesToUser(User user);

    /**
     * Retrieves a list of messages exchanged between two users.
     *
     * @param user1    The first user.
     * @param user2    The second user.
     * @param pageable Pageable object for pagination.
     * @return Optional containing a list of messages between the two users, if any.
     */
    @Query("SELECT dm FROM DirectMessage dm WHERE (dm.sender = :user1 AND dm.receiver = :user2) OR (dm.sender = :user2 AND dm.receiver = :user1) ORDER BY dm.timestamp DESC")
    Optional<List<DirectMessage>> findMessagesBetweenUsers(User user1, User user2, Pageable pageable);

}
