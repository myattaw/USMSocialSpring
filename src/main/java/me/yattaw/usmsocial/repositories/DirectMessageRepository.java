package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.message.DirectMessage;
import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {

    @Query("SELECT dm.receiver FROM DirectMessage dm WHERE dm.sender = :user")
    List<User> findUsersWithMessagesFromUser(User user);

    @Query("SELECT dm FROM DirectMessage dm WHERE (dm.sender = :user1 AND dm.receiver = :user2) OR (dm.sender = :user2 AND dm.receiver = :user1) ORDER BY dm.timestamp DESC")
    Optional<DirectMessage> findLastMessageBetweenUsers(User user1, User user2);

    @Query("SELECT dm FROM DirectMessage dm WHERE (dm.sender = :user1 AND dm.receiver = :user2) OR (dm.sender = :user2 AND dm.receiver = :user1) ORDER BY dm.timestamp DESC")
    Optional<List<DirectMessage>> findMessagesBetweenUsers(User user1, User user2, Pageable pageable);


}
