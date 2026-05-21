package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.BloodPressureAlert;

/**
 * Factory for creating blood pressure alerts.
 * Creates alerts for critical blood pressure thresholds or dangerous trends.
 */
public class BloodPressureAlertFactory implements AlertFactory {

    /**
     * Creates a new blood pressure alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert (e.g., "High systolic: 190 mmHg")
     * @param timestamp the time when the alert was created
     * @return a new BloodPressureAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}