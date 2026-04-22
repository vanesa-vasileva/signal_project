package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates random alerts for patients.
 * Each patient can either have an active alert (triggered) or not (resolved).
 * If an alert is active, there is a 90% chance it will resolve.
 * If no alert are active, there is a small chance a new one will appear.
 * <p>
 * It is used for simulating health events.
 */
public class AlertGenerator implements PatientDataGenerator {

    private static final Random RANDOM_GENERATOR = new Random();

    //0.9 was a magic number, now it is a constant
    private static final double RESOLUTION_PROBABILITY = 0.9;

    // 0.1 was a magic number, now it is a constant
    private static final double ALERT_RATE = 0.1;

    //changed to lowercase because variable names should be camelCase
    private boolean[] alertStates; // false = resolved, true = triggered

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert data for a single patient.
     * Either triggers a new alert or resolves an existing one.
     *
     * @param patientId the ID of the patient
     * @param outputStrategy the output strategy where the alert will be sent
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < RESOLUTION_PROBABILITY) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = ALERT_RATE; // Average rate (alerts per period), adjust based on desired frequency
                double probability = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < probability;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
