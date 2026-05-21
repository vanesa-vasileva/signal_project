package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.AlertFactory;

/**
 * Strategy for checking blood oxygen saturation levels.
 * Triggers an alert if oxygen saturation falls below 90%.
 */
public class OxygenSaturationStrategy implements AlertStrategy {
    private final AlertFactory factory = new BloodOxygenAlertFactory();

    /**
     * Checks an oxygen saturation reading and returns an alert if too low.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time when the reading was taken
     * @param value the oxygen saturation percentage (%)
     * @return Alert if value < 90, otherwise null
     */
    @Override
    public Alert checkAlert(String patientId, long timestamp, double value) {
        if (value < 92) {
            return factory.createAlert(patientId, "Low oxygen saturation: " + value + "%", timestamp);
        }
        return null;
    }
}