package me.yattaw.usmsocial.entities.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a user report request containing details such as
 * report type, reason, and target user ID.
 * @version 17 April 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReportRequest {

    /**
     * The type of report.
     */
    private ReportType reportType;

    /**
     * The reason for the report.
     */
    private String reason;

    /**
     * The ID of the user being reported.
     */
    private Integer targetId;

}
