package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.message.GroupMessage;
import me.yattaw.usmsocial.entities.report.UserReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

    List<GroupMessage> findTop30ByOrderByTimestampDesc(Pageable pageable);

}
