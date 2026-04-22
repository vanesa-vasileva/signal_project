package com.cardio_generator.outputs;

/**
 * Defines how patient data is sent out.
 * Implementations can write to console, files, etc.
 */
public interface OutputStrategy {
    /**
     * Sends a single data entry.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time when the data was generated
     * @param label the type of data
     * @param data the actual data value
     */
    void output(int patientId, long timestamp, String label, String data);
}
