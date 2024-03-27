package me.yattaw.usmsocial.entities.report;

import lombok.Getter;

@Getter
public enum ReportType {

    REPORTED_POST("Inappropriate Content", "Spamming", "Harassment", "Violence", "Misinformation"),
    REPORTED_COMMENT("Inappropriate Content", "Spamming", "Harassment", "False Information"),
    REPORTED_USER("Impersonation", "Harassment", "Hate Speech", "Fake Account", "Suspicious Activity");

    private String[] reasons;

    ReportType(String... reasons) {
        this.reasons = reasons;
    }

    public String getReasonById(int id) {
        if (id < 0 || id >= reasons.length) {
            throw new IllegalArgumentException("You must enter a valid reason id.");
        }
        return reasons[id];
    }

}
