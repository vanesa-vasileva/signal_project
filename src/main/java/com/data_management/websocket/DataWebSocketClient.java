package com.data_management.websocket;

import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebSocket client that connects to the cardio data simulator and stores incoming real-time data.
 * <p>
 * Expects messages in the format: patientId,timestamp,label,value
 * Example: "123,1700000000,HeartRate,75.5"
 * </p>
 */
public class DataWebSocketClient extends WebSocketClient {

    private final DataStorage storage;

    /**
     * Constructs a WebSocket client that connects to the given server URL.
     *
     * @param serverUrl the WebSocket server URL (e.g., "ws://localhost:8080")
     * @param storage   the DataStorage where received data will be saved
     * @throws URISyntaxException if the serverUrl is invalid
     */
    public DataWebSocketClient(String serverUrl, DataStorage storage) throws URISyntaxException {
        super(new URI(serverUrl));
        this.storage = storage;
    }

    /**
     * Called when the connection to the WebSocket server is successfully opened.
     *
     * @param handshakedata data from the handshake
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket server");
    }

    /**
     * Processes each incoming message, parses it, and stores it in DataStorage.
     * Malformed messages are logged but do not crash the client.
     *
     * @param message the raw message received from the server
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

    /**
     * Called when the connection is closed.
     *
     * @param code   status code
     * @param reason reason for closing
     * @param remote true if closed by the server, false if closed locally
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    /**
     * Called when an error occurs on the WebSocket connection.
     *
     * @param ex the exception that occurred
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }
}