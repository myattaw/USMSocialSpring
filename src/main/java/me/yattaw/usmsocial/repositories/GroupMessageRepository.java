package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.message.GroupMessage;
import me.yattaw.usmsocial.entities.report.UserReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing group messages in the database.
 */
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

    /**
     * Retrieves the top 30 group messages ordered by timestamp in descending order.
     *
     * @param pageable Pageable object for pagination.
     * @return List of the top 30 group messages ordered by timestamp in descending order.
     */
    List<GroupMessage> findTop30ByOrderByTimestampDesc(Pageable pageable);

}
