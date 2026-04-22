package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Represents a data generator for a single patient.
 * Each generator produces a specific type of health data.
 */
public interface PatientDataGenerator {
    /**
     * Generates health data for a patient and sends it to the output.
     *
     * @param patientId the ID of the patient
     * @param outputStrategy where to send the generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
