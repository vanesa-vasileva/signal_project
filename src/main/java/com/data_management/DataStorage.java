package com.data_management;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    private static final DataStorage instance = new DataStorage();

    private final ConcurrentHashMap<Integer, Patient> patients = new ConcurrentHashMap<>();

    private DataStorage() {}

    public static DataStorage getInstance() {
        return instance;
    }

    public void addPatient(Patient patient) {
        patients.put(patient.getPatientId(), patient);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public void addPatientData(int patientId, double value, String label, long timestamp) {
        // Get the patient, or create one if this id is new — done safely in a single step
        Patient patient = patients.computeIfAbsent(patientId, Patient::new);
        synchronized (patient) {   // only one thread writes to a given patient at a time
            patient.addRecord(value, label, timestamp);
        }
    }

    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patients.get(patientId);
        if (patient == null) {
            return new ArrayList<>();
        }
        synchronized (patient) {
            return patient.getRecords(startTime, endTime);
        }
    }
}