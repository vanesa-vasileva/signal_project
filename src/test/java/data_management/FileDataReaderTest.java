package data_management;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDataReaderTest {

    @TempDir
    Path tempDir;

    private DataStorage storage;
    private FileDataReader reader;

    @BeforeEach
    void setUp() {
        storage = new DataStorage();
    }

    private void createTempFile(String filename, String content) throws IOException {
        File file = tempDir.resolve(filename).toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    @Test
    void testReadsValidFileCorrectly() throws IOException {
        createTempFile("data.txt",
                "1,1000000,HeartRate,75.0\n" +
                        "2,2000000,SystolicPressure,120.0\n");

        reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(1, 0, Long.MAX_VALUE);
        assertFalse(records.isEmpty());
    }

    @Test
    void testHandlesEmptyDirectory() {
        reader = new FileDataReader(tempDir.toString());
        assertDoesNotThrow(() -> reader.readData(storage));
    }

    @Test
    void testSkipsMalformedLines() throws IOException {
        createTempFile("data.txt",
                "1,1000000,HeartRate,75.0\n" +
                        "THIS IS BAD DATA\n" +
                        "2,2000000,HeartRate,80.0\n");

        reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        List<PatientRecord> p1 = storage.getRecords(1, 0, Long.MAX_VALUE);
        assertFalse(p1.isEmpty());
    }

    @Test
    void testThrowsExceptionForInvalidDirectory() {
        reader = new FileDataReader("/this/path/does/not/exist");
        assertThrows(IOException.class, () -> reader.readData(storage));
    }

    @Test
    void testReadsMultipleFiles() throws IOException {
        createTempFile("fileA.txt", "1,1000000,HeartRate,70.0\n");
        createTempFile("fileB.txt", "2,1000000,HeartRate,90.0\n");

        reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        assertFalse(storage.getRecords(1, 0, Long.MAX_VALUE).isEmpty());
        assertFalse(storage.getRecords(2, 0, Long.MAX_VALUE).isEmpty());
    }
}
