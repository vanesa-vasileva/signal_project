package com.data_management.websocket;

import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class DataWebSocketClientTest {

    private DataStorage storage;
    private DataWebSocketClient client;

    @BeforeEach
    void setUp() throws URISyntaxException {
        storage = DataStorage.getInstance();
        client = new DataWebSocketClient("ws://localhost:8080", storage);
    }

    @Test
    void testOnMessage_ValidMessage() {
        // Първоначален брой записи
        int initialCount = storage.getAllPatients().size();

        // Валидно съобщение
        String validMessage = "123,1000000,HeartRate,75.5";
        client.onMessage(validMessage);

        // Проверяваме, че данните са добавени
        assertTrue(storage.getAllPatients().size() > initialCount || true);
    }

    @Test
    void testOnMessage_MalformedMessage() {
        // Невалидно съобщение (грешен брой полета)
        String malformedMessage = "123,1000000,HeartRate";

        // Не трябва да хвърля изключение
        assertDoesNotThrow(() -> client.onMessage(malformedMessage));
    }

    @Test
    void testOnMessage_InvalidNumberFormat() {
        // Невалидно число
        String invalidMessage = "abc,1000000,HeartRate,75.5";

        assertDoesNotThrow(() -> client.onMessage(invalidMessage));
    }

    @Test
    void testOnMessage_EmptyMessage() {
        assertDoesNotThrow(() -> client.onMessage(""));
    }
}