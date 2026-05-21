package com.alerts;

/**
 * Alert specifically for blood oxygen level issues.
 * Triggered by low oxygen saturation or rapid oxygen drops.
 */
public class BloodOxygenAlert extends Alert {

    /**
     * Creates a new blood oxygen alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert (e.g., "Low oxygen: 85%")
     * @param timestamp the time when the alert was created
     */
    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}