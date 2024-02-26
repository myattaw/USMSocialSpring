package me.yattaw.usmsocial.entities.report;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usm_social_reports")
public class UserReport {

    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "report_type")
    private ReportType reportType;

    @Column(name = "reported_id")
    private Integer reported;

    @Column(length = 280)
    private String reason;

    private LocalDateTime timestamp;

}