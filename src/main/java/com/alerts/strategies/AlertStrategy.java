package com.alerts.strategies;

import com.alerts.Alert;

public interface AlertStrategy {
    Alert checkAlert(String patientId, long timestamp, double value);
}
