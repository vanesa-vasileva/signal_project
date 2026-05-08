package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.ECGAlertFactory;
import com.alerts.factories.AlertFactory;

public class HeartRateStrategy implements AlertStrategy {
    private final AlertFactory factory = new ECGAlertFactory();

    @Override
    public Alert checkAlert(String patientId, long timestamp, double value) {
        if (value > 120 || value < 40) {
            return factory.createAlert(patientId, "Abnormal heart rate: " + value + " bpm", timestamp);
        }
        return null;
    }
}
