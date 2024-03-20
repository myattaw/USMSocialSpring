package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.message.DirectMessage;
import me.yattaw.usmsocial.entities.report.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {

}
