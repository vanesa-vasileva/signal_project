package com.data_management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RealTimeDataReaderTest {

    @Test
    void testConstructor() {
        RealTimeDataReader reader = new RealTimeDataReader("ws://localhost:8080");
        assertNotNull(reader);
    }

    @Test
    void testStopReading() {
        RealTimeDataReader reader = new RealTimeDataReader("ws://localhost:8080");
        assertDoesNotThrow(() -> reader.stopReading());
    }
}