package me.yattaw.usmsocial.repositories;

import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.user.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for UserGroups.
 */
public interface UserGroupRepository extends JpaRepository<UserGroups, Integer> {

}
