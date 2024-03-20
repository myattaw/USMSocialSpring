package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.user.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroups, Integer> {

}
