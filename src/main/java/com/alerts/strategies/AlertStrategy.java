package com.alerts.strategies;

import com.alerts.Alert;

/**
 * Strategy interface for checking patient data and determining if an alert should be triggered.
 * Different implementations check different health metrics (blood pressure, heart rate, oxygen levels).
 */
public interface AlertStrategy {

    /**
     * Checks a single data value and returns an alert if the condition is met.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time when the data was recorded
     * @param value the measured value (e.g., heart rate in bpm, oxygen saturation in %)
     * @return an Alert if the condition is triggered, null otherwise
     */
    Alert checkAlert(String patientId, long timestamp, double value);
}