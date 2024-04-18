package me.yattaw.usmsocial.entities.report;

import lombok.Getter;

/**
 * Define the reasons a <code>User</code> may be reported.
 * @version 17 April 2024
 */
@Getter
public enum ReportType {

    /**
     * Reasons for reporting a post.
     */
    REPORTED_POST("Inappropriate Content", "Spamming", "Harassment", "Violence", "Misinformation"),
    /**
     * Reasons for reporting a comment.
     */
    REPORTED_COMMENT("Inappropriate Content", "Spamming", "Harassment", "False Information"),
    /**
     * Reasons for reporting a sender.
     */
    REPORTED_USER("Impersonation", "Harassment", "Hate Speech", "Fake Account", "Suspicious Activity");

    /**
     * Store the list of reasons for reports.
     */
    private String[] reasons;

    /**
     * Constructor for creating a ReportType object with one or more reasons.
     * @param reasons The reasons associated with the report type.
     *                Can be one or more strings.
     */
    ReportType(String... reasons) {
        this.reasons = reasons;
    }

    /**
     * Retrieves the reason associated with the specified ID.
     *
     * @param id The ID of the reason to retrieve.
     * @return The reason associated with the specified ID.
     * @throws IllegalArgumentException If the provided ID is negative or
     *         greater than or equal to the total number of reasons.
     */
    public String getReasonById(int id) {
        if (id < 0 || id >= reasons.length) {
            throw new IllegalArgumentException("You must enter a valid reason id.");
        }
        return reasons[id];
    }

}
