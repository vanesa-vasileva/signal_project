package com.data_management;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static DataStorage instance;
    private List<Patient> patients;

    private DataStorage() {
        patients = new ArrayList<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public List<Patient> getAllPatients() {
        return patients;
    }

    public void addPatientData(int patientId, double value, String label, long timestamp) {
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patients.add(patient);
        }
        patient.addRecord(value, label, timestamp);
    }

    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            return new ArrayList<>();
        }
        return patient.getRecords(startTime, endTime);
    }

    private Patient findPatientById(int patientId) {
        for (Patient p : patients) {
            if (p.getPatientId() == patientId) {
                return p;
            }
        }
        return null;
    }
}