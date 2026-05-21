package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.AlertFactory;

/**
 * Strategy for checking blood pressure values.
 * Triggers an alert if systolic pressure is above 180 or below 50 mmHg.
 */
public class BloodPressureStrategy implements AlertStrategy {
    private final AlertFactory factory = new BloodPressureAlertFactory();

    /**
     * Checks a blood pressure reading and returns an alert if critical.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time when the reading was taken
     * @param value the systolic blood pressure value in mmHg
     * @return Alert if value > 180 or value < 50, otherwise null
     */
    @Override
    public Alert checkAlert(String patientId, long timestamp, double value) {
        if (value > 180 || value < 90) {
            return factory.createAlert(patientId, "Critical blood pressure: " + value, timestamp);
        }
        return null;
    }
}