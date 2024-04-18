package me.yattaw.usmsocial.admin.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.report.ReportType;

import java.time.LocalDateTime;

/**
 * Represents a response object for user reports.
 *
 * <p>
 * This class encapsulates information about a user report, including the report type, reason,
 * the first and last name of the user being reported, and the timestamp of the report.
 * </p>
 *
 * @version 17 April 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {

    /**
     * The type of report.
     */
    private ReportType reportType;

    /**
     * The reason for the report.
     */
    private String reportReason;

    /**
     * The first name of the user being reported.
     */
    private String firstName;

    /**
     * The last name of the user being reported.
     */
    private String lastName;

    /**
     * The timestamp when the report was made.
     */
    private LocalDateTime timestamp;

}