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

/**
 * Repository interface for managing user reports in the database.
 */
public interface ReportRepository extends JpaRepository<UserReport, Integer> {

    /**
     * Retrieves all user reports of a specific report type.
     *
     * @param reportType The type of report.
     * @return A list of user reports.
     */
    @Query("SELECT ur.reported FROM UserReport ur WHERE ur.reportType = :reportType")
    List<UserReport> findAllReports(ReportType reportType);

}
