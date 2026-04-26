package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(42);
        patient.addRecord(75.0,  "HeartRate",        1000L);
        patient.addRecord(80.0,  "HeartRate",        2000L);
        patient.addRecord(120.0, "SystolicPressure", 3000L);
        patient.addRecord(95.0,  "Saturation",       4000L);
    }

    @Test
    void testGetRecordsReturnsAllWithinRange() {
        List<PatientRecord> result = patient.getRecords(1000L, 4000L);
        assertEquals(4, result.size());
    }

    @Test
    void testGetRecordsFiltersCorrectly() {
        List<PatientRecord> result = patient.getRecords(1000L, 2000L);
        assertEquals(2, result.size());
    }

    @Test
    void testGetRecordsExcludesOutsideRange() {
        List<PatientRecord> result = patient.getRecords(5000L, 9000L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRecordsIncludesBoundaryTimestamps() {
        // The boundary values themselves should be included
        List<PatientRecord> result = patient.getRecords(1000L, 1000L);
        assertEquals(1, result.size());
        assertEquals(75.0, result.get(0).getMeasurementValue());
    }

    @Test
    void testGetRecordsReturnsEmptyListNotNull() {
        List<PatientRecord> result = patient.getRecords(9999L, 99999L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAddRecordIncreasesRecordCount() {
        // Before adding: 4 records in range 1000-4000
        patient.addRecord(60.0, "HeartRate", 5000L);
        // Now there should be 1 record in the 5000-5000 range
        List<PatientRecord> result = patient.getRecords(5000L, 5000L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetRecordsReturnsCorrectRecordType() {
        List<PatientRecord> result = patient.getRecords(3000L, 3000L);
        assertEquals(1, result.size());
        assertEquals("SystolicPressure", result.get(0).getRecordType());
    }
}
