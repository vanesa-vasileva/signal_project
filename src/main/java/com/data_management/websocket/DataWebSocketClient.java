package com.data_management.websocket;

import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocket client that connects to the cardio data simulator and stores incoming data.
 * Messages are expected in format: patientId,timestamp,label,value
 */
public class DataWebSocketClient extends WebSocketClient {

    private final DataStorage storage;

    /**
     * Creates a new WebSocket client.
     *
     * @param serverUrl the WebSocket server URL (e.g., "ws://localhost:8080")
     * @param storage the DataStorage where received data will be saved
     * @throws URISyntaxException if the serverUrl is invalid
     */
    public DataWebSocketClient(String serverUrl, DataStorage storage) throws URISyntaxException {
        super(new URI(serverUrl));
        this.storage = storage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket server");
    }

    /**
     * Handles incoming messages, parses them, and stores them in DataStorage.
     * Expected format: patientId,timestamp,label,data
     */
    @Override
    public void onMessage(String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length != 4) {
                System.err.println("Malformed message: " + message);
                return;
            }

            int patientId = Integer.parseInt(parts[0].trim());
            long timestamp = Long.parseLong(parts[1].trim());
            String label = parts[2].trim();
            double value = Double.parseDouble(parts[3].trim());

            storage.addPatientData(patientId, value, label, timestamp);

        } catch (NumberFormatException e) {
            System.err.println("Error parsing message: " + message);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }
}