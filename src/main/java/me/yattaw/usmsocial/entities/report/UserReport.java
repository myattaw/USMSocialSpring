package me.yattaw.usmsocial.entities.report;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Defines a user report with details such as report type,
 * reported user ID, reason, and timestamo.
 * @version 17 April, 2024
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_reports")
public class UserReport {

    /**
     * id - the unique identifier for the report.
     */
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * reportType - the type of report.
     */
    @Column(name = "report_type")
    private ReportType reportType;

    /**
     * reported - the ID of the user being reported.
     */
    @Column(name = "reported_id")
    private Integer reported;

    /**
     * reason - the reason for this report.
     */
    @Column(length = 280)
    private String reason;

    /**
     * timestamp - the time this report was created.
     */
    private LocalDateTime timestamp;

}