package com.alerts.factories;

import com.alerts.Alert;

/**
 * Factory interface for creating Alert objects.
 * Implementations create specific types of alerts (e.g., blood pressure, ECG).
 */
public interface AlertFactory {
    // Modelled as an interface rather than a base class: the factories share no
// common state, so an interface keeps them decoupled while still defining
// the createAlert contract required by the Factory Method pattern.
    /**
     * Creates a new Alert instance.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the time when the alert was created
     * @return a new Alert object
     */
    Alert createAlert(String patientId, String condition, long timestamp);
}