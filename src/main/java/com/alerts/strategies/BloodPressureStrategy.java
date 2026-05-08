package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.AlertFactory;

public class BloodPressureStrategy implements AlertStrategy {
    private final AlertFactory factory = new BloodPressureAlertFactory();

    @Override
    public Alert checkAlert(String patientId, long timestamp, double value) {
        if (value > 180 || value < 50) {
            return factory.createAlert(patientId, "Critical blood pressure: " + value, timestamp);
        }
        return null;
    }
}
