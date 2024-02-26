package me.yattaw.usmsocial.entities.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReportRequest {

    private ReportType reportType; // add report index later once we add wiki
    private String reason;
    private Integer targetId;

}
