package com.alerts;

/**
 * Alert specifically for blood pressure anomalies.
 * Triggered by critical thresholds or dangerous trends
 */
public class BloodPressureAlert extends Alert {

    /**
     * Creates a new blood pressure alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the time when the alert was created
     */
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}