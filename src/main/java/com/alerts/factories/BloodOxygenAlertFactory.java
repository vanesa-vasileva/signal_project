package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;

/**
 * Factory for creating blood oxygen level alerts.
 * Creates alerts for low oxygen saturation or rapid oxygen drops.
 */
public class BloodOxygenAlertFactory implements AlertFactory {

    /**
     * Creates a new blood oxygen alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert (e.g., "Low oxygen saturation: 85%")
     * @param timestamp the time when the alert was created
     * @return a new BloodOxygenAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}