package com.data_management;

import com.cardio_generator.HealthDataSimulator;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WebSocketIntegrationTest {

    @Test
    void testWebSocketIntegration() throws Exception {
        // Starts the simulator in a separate thread
        Thread simulatorThread = new Thread(() -> {
            try {
                HealthDataSimulator.main(new String[]{"--output", "websocket:8080", "--patient-count", "3"});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        simulatorThread.start();

        // Give the server some time to start
        Thread.sleep(2000);

        DataStorage storage = DataStorage.getInstance();
        int initialPatientCount = storage.getAllPatients().size();

        RealTimeDataReader reader = new RealTimeDataReader("ws://localhost:8080");

        // We only test if connection is possible, not long data retention
        Thread clientThread = new Thread(() -> {
            try {
                reader.readData(storage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        clientThread.start();

        // Let the client run for a few seconds
        Thread.sleep(5000);

        reader.stopReading();
        simulatorThread.interrupt();

        // Verify we received data
        assertTrue(storage.getAllPatients().size() > initialPatientCount,
                "Expected at least one patient record after receiving data");
    }
}