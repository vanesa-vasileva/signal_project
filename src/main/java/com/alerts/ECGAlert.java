package com.alerts;

/**
 * Alert specifically for ECG anomalies.
 * Triggered by abnormal heart rates
 * or irregular rhythms detected through peak analysis.
 */
public class ECGAlert extends Alert {

    /**
     * Creates a new ECG alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the time when the alert was created
     */
    public ECGAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}