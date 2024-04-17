package me.yattaw.usmsocial.repositories;

import jakarta.transaction.Transactional;
import me.yattaw.usmsocial.entities.report.ReportType;
import me.yattaw.usmsocial.entities.report.UserReport;
import me.yattaw.usmsocial.entities.user.User;
import me.yattaw.usmsocial.entities.user.UserPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<UserReport, Integer> {

    @Query("SELECT ur.reported FROM UserReport ur WHERE ur.reportType = :reportType")
    List<UserReport> findAllReports(ReportType reportType);

}
