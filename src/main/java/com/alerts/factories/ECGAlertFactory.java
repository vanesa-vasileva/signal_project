package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.ECGAlert;

/**
 * Factory for creating ECG (heart rate) alerts.
 * Creates alerts for abnormal heart rates or irregular rhythms.
 */
public class ECGAlertFactory implements AlertFactory {

    /**
     * Creates a new ECG alert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert (e.g., "Heart rate > 120 bpm")
     * @param timestamp the time when the alert was created
     * @return a new ECGAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}