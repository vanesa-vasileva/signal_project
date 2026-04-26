package data_management;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    private DataStorage storage;
    private AlertGenerator generator;
    private Patient patient;

    @BeforeEach
    void setUp() {
        storage   = new DataStorage();
        generator = new AlertGenerator(storage);
        patient   = new Patient(1);
    }

    // ── Blood pressure trend ──────────────────────────────────────────────────

    @Test
    void testIncreasingSystolicTrendTriggersAlert() {
        patient.addRecord(100.0, "SystolicPressure", 1000L);
        patient.addRecord(115.0, "SystolicPressure", 2000L);
        patient.addRecord(130.0, "SystolicPressure", 3000L);

        generator.evaluateData(patient);

        assertTrue(alertContains(generator.getTriggeredAlerts(), "Increasing Trend"));
    }

    @Test
    void testDecreasingDiastolicTrendTriggersAlert() {
        patient.addRecord(100.0, "DiastolicPressure", 1000L);
        patient.addRecord(85.0,  "DiastolicPressure", 2000L);
        patient.addRecord(70.0,  "DiastolicPressure", 3000L);

        generator.evaluateData(patient);

        assertTrue(alertContains(generator.getTriggeredAlerts(), "Decreasing Trend"));
    }

    @Test
    void testSmallChangeDoesNotTriggerTrendAlert() {
        patient.addRecord(100.0, "SystolicPressure", 1000L);
        patient.addRecord(105.0, "SystolicPressure", 2000L);
        patient.addRecord(110.0, "SystolicPressure", 3000L);

        generator.evaluateData(patient);

        assertFalse(alertContains(generator.getTriggeredAlerts(), "Trend"));
    }

    // ── Critical threshold ────────────────────────────────────────────────────

    @Test
    void testCriticalHighSystolicTriggersAlert() {
        patient.addRecord(185.0, "SystolicPressure", 1000L);
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Critical High Systolic"));
    }

    @Test
    void testCriticalLowSystolicTriggersAlert() {
        patient.addRecord(85.0, "SystolicPressure", 1000L);
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Critical Low Systolic"));
    }

    @Test
    void testCriticalHighDiastolicTriggersAlert() {
        patient.addRecord(125.0, "DiastolicPressure", 1000L);
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Critical High Diastolic"));
    }

    @Test
    void testCriticalLowDiastolicTriggersAlert() {
        patient.addRecord(55.0, "DiastolicPressure", 1000L);
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Critical Low Diastolic"));
    }

    @Test
    void testNormalBPDoesNotTriggerAlert() {
        patient.addRecord(120.0, "SystolicPressure",  1000L);
        patient.addRecord(80.0,  "DiastolicPressure", 1000L);
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "Critical"));
    }

    // ── Saturation ────────────────────────────────────────────────────────────

    @Test
    void testLowSaturationTriggersAlert() {
        patient.addRecord(88.0, "Saturation", 1000L);
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Low Blood Oxygen"));
    }

    @Test
    void testSaturationAt92DoesNotTriggerAlert() {
        patient.addRecord(92.0, "Saturation", 1000L);
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "Low Blood Oxygen"));
    }

    @Test
    void testRapidSaturationDropTriggersAlert() {
        patient.addRecord(98.0, "Saturation", 1000L);
        patient.addRecord(92.0, "Saturation", 1000L + 300_000L); // 5 minutes later
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Rapid Blood Oxygen"));
    }

    @Test
    void testSaturationDropOutsideWindowDoesNotTrigger() {
        patient.addRecord(98.0, "Saturation", 1000L);
        patient.addRecord(92.0, "Saturation", 1000L + 900_000L); // 15 minutes later
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "Rapid Blood Oxygen"));
    }

    // ── Combined hypotensive hypoxemia ────────────────────────────────────────

    @Test
    void testHypotensiveHypoxemiaTriggersWhenBothConditionsMet() {
        patient.addRecord(85.0, "SystolicPressure", 1000L);
        patient.addRecord(88.0, "Saturation",       1000L);
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Hypotensive Hypoxemia"));
    }

    @Test
    void testHypotensiveHypoxemiaDoesNotTriggerWithOnlyLowBP() {
        patient.addRecord(85.0, "SystolicPressure", 1000L);
        patient.addRecord(96.0, "Saturation",       1000L);
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "Hypotensive Hypoxemia"));
    }

    @Test
    void testHypotensiveHypoxemiaDoesNotTriggerWithOnlyLowSaturation() {
        patient.addRecord(120.0, "SystolicPressure", 1000L);
        patient.addRecord(88.0,  "Saturation",       1000L);
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "Hypotensive Hypoxemia"));
    }

    // ── ECG ───────────────────────────────────────────────────────────────────

    @Test
    void testAbnormalECGPeakTriggersAlert() {
        for (int i = 0; i < 10; i++) {
            patient.addRecord(1.0, "ECG", 1000L + i * 100L);
        }
        patient.addRecord(3.0, "ECG", 1000L + 10 * 100L); // spike
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "ECG Peak"));
    }

    @Test
    void testNormalECGDoesNotTriggerAlert() {
        for (int i = 0; i < 15; i++) {
            patient.addRecord(1.0 + (i % 2) * 0.1, "ECG", 1000L + i * 100L);
        }
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "ECG Peak"));
    }

    // ── Manual alert ──────────────────────────────────────────────────────────

    @Test
    void testManualAlertButtonTriggersAlert() {
        patient.addRecord(1.0, "Alert", 1000L);
        generator.evaluateData(patient);
        assertTrue(alertContains(generator.getTriggeredAlerts(), "Manual Alert Button"));
    }

    @Test
    void testUntriggeredAlertDoesNotFire() {
        patient.addRecord(0.0, "Alert", 1000L);
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "Manual Alert Button"));
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    void testNoRecordsProducesNoAlerts() {
        generator.evaluateData(patient);
        assertTrue(generator.getTriggeredAlerts().isEmpty());
    }

    @Test
    void testFewerThanThreeReadingsDoesNotTriggerTrend() {
        patient.addRecord(100.0, "SystolicPressure", 1000L);
        patient.addRecord(115.0, "SystolicPressure", 2000L);
        generator.evaluateData(patient);
        assertFalse(alertContains(generator.getTriggeredAlerts(), "Trend"));
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    private boolean alertContains(List<Alert> alerts, String keyword) {
        return alerts.stream()
                .anyMatch(a -> a.getCondition().toLowerCase()
                        .contains(keyword.toLowerCase()));
    }
}
