package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.ECGAlertFactory;
import com.alerts.factories.AlertFactory;

/**
 * Strategy for checking heart rate values.
 * Triggers an alert if heart rate is above 120 bpm or below 40 bpm.
 */
public class HeartRateStrategy implements AlertStrategy {
    private final AlertFactory factory = new ECGAlertFactory();

    /**
     * Checks a heart rate reading and returns an alert if abnormal.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time when the reading was taken
     * @param value the heart rate in beats per minute (bpm)
     * @return Alert if value > 120 or value < 40, otherwise null
     */
    @Override
    public Alert checkAlert(String patientId, long timestamp, double value) {
        if (value > 120 || value < 40) {
            return factory.createAlert(patientId, "Abnormal heart rate: " + value + " bpm", timestamp);
        }
        return null;
    }
}