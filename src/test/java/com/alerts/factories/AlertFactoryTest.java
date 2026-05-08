package com.alerts.factories;

import com.alerts.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlertFactoryTest {

    @Test
    public void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("P001", "High BP", 123456789L);

        assertNotNull(alert);
        assertTrue(alert instanceof BloodPressureAlert);
        assertEquals("P001", alert.getPatientId());
        assertEquals("High BP", alert.getCondition());
        assertEquals(123456789L, alert.getTimestamp());
    }

    @Test
    public void testBloodOxygenAlertFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("P002", "Low Oxygen", 987654321L);

        assertNotNull(alert);
        assertTrue(alert instanceof BloodOxygenAlert);
    }

    @Test
    public void testECGAlertFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("P003", "Arrhythmia", 555555555L);

        assertNotNull(alert);
        assertTrue(alert instanceof ECGAlert);
    }
}
