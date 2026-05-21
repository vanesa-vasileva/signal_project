package com.alerts;

/**
 * Represents a medical alert generated for a patient.
 * Contains the patient ID, the condition that triggered the alert,
 * and the timestamp when the alert was created.
 */
public class Alert {
    private String patientId;
    private String condition;
    private long timestamp;

    /**
     * Creates a new alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the time when the alert was created (milliseconds since epoch)
     */
    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Returns the patient ID.
     *
     * @return the patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Returns the condition that triggered the alert.
     *
     * @return the condition description
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Returns the timestamp when the alert was created.
     *
     * @return the timestamp in milliseconds since epoch
     */
    public long getTimestamp() {
        return timestamp;
    }
}