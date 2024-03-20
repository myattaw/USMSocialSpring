package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.message.GroupMessage;
import me.yattaw.usmsocial.entities.report.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

}
