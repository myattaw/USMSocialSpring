package me.yattaw.usmsocial.admin.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yattaw.usmsocial.entities.report.ReportType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {

    private ReportType reportType;
    private String reportReason;
    private String firstName;
    private String lastName;
    private LocalDateTime timestamp;

}