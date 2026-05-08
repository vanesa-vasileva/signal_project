package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.AlertFactory;

public class OxygenSaturationStrategy implements AlertStrategy {
    private final AlertFactory factory = new BloodOxygenAlertFactory();

    @Override
    public Alert checkAlert(String patientId, long timestamp, double value) {
        if (value < 90) {
            return factory.createAlert(patientId, "Low oxygen saturation: " + value + "%", timestamp);
        }
        return null;
    }
}
