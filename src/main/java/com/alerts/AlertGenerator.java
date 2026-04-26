package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Analyses patient data stored in DataStorage and triggers alerts
 * when any of the defined clinical conditions are detected.
 *
 * Alerts covered:
 *   1. Blood pressure trend (rising or falling by more than 10 mmHg across 3 readings)
 *   2. Blood pressure critical threshold (systolic >180/<90, diastolic >120/<60)
 *   3. Low blood oxygen saturation (below 92%)
 *   4. Rapid saturation drop (5% or more within 10 minutes)
 *   5. Combined hypotensive hypoxemia (low BP + low saturation simultaneously)
 *   6. Abnormal ECG peak (reading more than 50% above sliding window average)
 *   7. Manually triggered alert (nurse or patient presses alert button)
 */
public class AlertGenerator {

    private final DataStorage dataStorage;

    // Keeps a record of every alert that has been fired in this session
    private final List<Alert> triggeredAlerts = new ArrayList<>();

    // ── Threshold constants ───────────────────────────────────────────────────
    private static final double BP_TREND_DELTA        = 10.0;  // mmHg per step
    private static final double SYSTOLIC_HIGH         = 180.0;
    private static final double SYSTOLIC_LOW          = 90.0;
    private static final double DIASTOLIC_HIGH        = 120.0;
    private static final double DIASTOLIC_LOW         = 60.0;
    private static final double SATURATION_LOW        = 92.0;  // percent
    private static final double SATURATION_DROP       = 5.0;   // percent
    private static final long   TEN_MINUTES_MS        = 600_000L;
    private static final int    ECG_WINDOW_SIZE       = 10;    // readings
    private static final double ECG_PEAK_MULTIPLIER   = 1.5;   // 50% above average

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Runs all alert checks against the supplied patient's records.
     * Called once per patient per evaluation cycle.
     *
     * @param patient the patient whose data should be evaluated
     */
    public void evaluateData(Patient patient) {
        int patientId = patient.getPatientId();

        // Fetch the complete record history for this patient
        List<PatientRecord> all = patient.getRecords(0, Long.MAX_VALUE);

        List<PatientRecord> systolic   = filterAndSort(all, "SystolicPressure");
        List<PatientRecord> diastolic  = filterAndSort(all, "DiastolicPressure");
        List<PatientRecord> saturation = filterAndSort(all, "Saturation");
        List<PatientRecord> ecg        = filterAndSort(all, "ECG");
        List<PatientRecord> alerts     = filterAndSort(all, "Alert");

        // Run each alert category
        checkBloodPressureTrend(patientId, systolic,  "Systolic");
        checkBloodPressureTrend(patientId, diastolic, "Diastolic");
        checkCriticalThreshold(patientId, systolic, diastolic);
        checkLowSaturation(patientId, saturation);
        checkRapidSaturationDrop(patientId, saturation);
        checkHypotensiveHypoxemia(patientId, systolic, saturation);
        checkEcgPeak(patientId, ecg);
        checkTriggeredAlert(patientId, alerts);
    }

    /**
     * Returns an unmodifiable view of all alerts fired so far.
     */
    public List<Alert> getTriggeredAlerts() {
        return List.copyOf(triggeredAlerts);
    }

    // ── Internal alert checks ─────────────────────────────────────────────────

    /**
     * Triggers an alert if three consecutive readings each differ
     * from the previous by more than 10 mmHg in the same direction.
     * Only the most recent window of three is checked.
     */
    private void checkBloodPressureTrend(int patientId,
                                         List<PatientRecord> records,
                                         String type) {
        if (records.size() < 3) return;

        // Start from the most recent triplet
        int last = records.size() - 1;
        double v1 = records.get(last - 2).getMeasurementValue();
        double v2 = records.get(last - 1).getMeasurementValue();
        double v3 = records.get(last).getMeasurementValue();

        if ((v2 - v1) > BP_TREND_DELTA && (v3 - v2) > BP_TREND_DELTA) {
            triggerAlert(new Alert(
                    String.valueOf(patientId),
                    type + " Blood Pressure Increasing Trend",
                    records.get(last).getTimestamp()));

        } else if ((v1 - v2) > BP_TREND_DELTA && (v2 - v3) > BP_TREND_DELTA) {
            triggerAlert(new Alert(
                    String.valueOf(patientId),
                    type + " Blood Pressure Decreasing Trend",
                    records.get(last).getTimestamp()));
        }
    }

    /**
     * Triggers an alert for any single reading that exceeds the critical thresholds.
     */
    private void checkCriticalThreshold(int patientId,
                                        List<PatientRecord> systolic,
                                        List<PatientRecord> diastolic) {
        for (PatientRecord r : systolic) {
            double v = r.getMeasurementValue();
            if (v > SYSTOLIC_HIGH) {
                triggerAlert(new Alert(String.valueOf(patientId),
                        "Critical High Systolic BP: " + v, r.getTimestamp()));
            } else if (v < SYSTOLIC_LOW) {
                triggerAlert(new Alert(String.valueOf(patientId),
                        "Critical Low Systolic BP: " + v, r.getTimestamp()));
            }
        }

        for (PatientRecord r : diastolic) {
            double v = r.getMeasurementValue();
            if (v > DIASTOLIC_HIGH) {
                triggerAlert(new Alert(String.valueOf(patientId),
                        "Critical High Diastolic BP: " + v, r.getTimestamp()));
            } else if (v < DIASTOLIC_LOW) {
                triggerAlert(new Alert(String.valueOf(patientId),
                        "Critical Low Diastolic BP: " + v, r.getTimestamp()));
            }
        }
    }

    /**
     * Triggers an alert for any saturation reading below 92%.
     */
    private void checkLowSaturation(int patientId, List<PatientRecord> records) {
        for (PatientRecord r : records) {
            if (r.getMeasurementValue() < SATURATION_LOW) {
                triggerAlert(new Alert(String.valueOf(patientId),
                        "Low Blood Oxygen Saturation: " + r.getMeasurementValue() + "%",
                        r.getTimestamp()));
            }
        }
    }

    /**
     * Triggers an alert if saturation drops by 5% or more within any 10-minute window.
     * Uses a two-pointer approach over the time-sorted records.
     */
    private void checkRapidSaturationDrop(int patientId, List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                long timeDiff = records.get(j).getTimestamp()
                        - records.get(i).getTimestamp();

                if (timeDiff > TEN_MINUTES_MS) break; // outside window

                double drop = records.get(i).getMeasurementValue()
                        - records.get(j).getMeasurementValue();

                if (drop >= SATURATION_DROP) {
                    triggerAlert(new Alert(String.valueOf(patientId),
                            "Rapid Blood Oxygen Saturation Drop",
                            records.get(j).getTimestamp()));
                }
            }
        }
    }

    /**
     * Triggers a combined hypotensive hypoxemia alert when both
     * systolic BP < 90 mmHg AND saturation < 92% are present
     * in the patient's most recent records.
     */
    private void checkHypotensiveHypoxemia(int patientId,
                                           List<PatientRecord> systolic,
                                           List<PatientRecord> saturation) {
        boolean lowBP  = systolic.stream()
                .anyMatch(r -> r.getMeasurementValue() < SYSTOLIC_LOW);
        boolean lowSat = saturation.stream()
                .anyMatch(r -> r.getMeasurementValue() < SATURATION_LOW);

        if (lowBP && lowSat) {
            triggerAlert(new Alert(String.valueOf(patientId),
                    "Hypotensive Hypoxemia Alert",
                    System.currentTimeMillis()));
        }
    }

    /**
     * Uses a sliding window of size 10 to compute a moving average of ECG values.
     * An alert fires when a reading exceeds 150% of the current window average,
     * indicating an abnormal peak.
     */
    private void checkEcgPeak(int patientId, List<PatientRecord> records) {
        if (records.size() <= ECG_WINDOW_SIZE) return;

        for (int i = ECG_WINDOW_SIZE; i < records.size(); i++) {
            double sum = 0;
            for (int j = i - ECG_WINDOW_SIZE; j < i; j++) {
                sum += records.get(j).getMeasurementValue();
            }
            double windowAverage = sum / ECG_WINDOW_SIZE;
            double current       = records.get(i).getMeasurementValue();

            if (windowAverage != 0 && current > windowAverage * ECG_PEAK_MULTIPLIER) {
                triggerAlert(new Alert(String.valueOf(patientId),
                        "Abnormal ECG Peak Detected",
                        records.get(i).getTimestamp()));
            }
        }
    }

    /**
     * Detects manual alert button presses emitted by the HealthDataGenerator.
     * A value of 1.0 means the alert is active; 0.0 means it was untriggered.
     */
    private void checkTriggeredAlert(int patientId, List<PatientRecord> records) {
        for (PatientRecord r : records) {
            if (r.getMeasurementValue() == 1.0) {
                triggerAlert(new Alert(String.valueOf(patientId),
                        "Manual Alert Button Triggered",
                        r.getTimestamp()));
            }
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /**
     * Records an alert and prints it to the console.
     * Protected so tests can override or spy on it if needed.
     */
    protected void triggerAlert(Alert alert) {
        triggeredAlerts.add(alert);
        System.out.println("[ALERT] Patient " + alert.getPatientId()
                + " | " + alert.getCondition()
                + " | " + alert.getTimestamp());
    }

    /**
     * Returns records of the given type, sorted by timestamp ascending.
     */
    private List<PatientRecord> filterAndSort(List<PatientRecord> records, String type) {
        return records.stream()
                .filter(r -> r.getRecordType().equalsIgnoreCase(type))
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .collect(Collectors.toList());
    }
}
