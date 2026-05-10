package com.data_management;

import com.data_management.websocket.DataWebSocketClient;

/**
 * Reads real-time patient data from a WebSocket server.
 * Implements the DataReader interface for streaming data.
 */
public class RealTimeDataReader implements DataReader {

    private final String serverUrl;
    private DataWebSocketClient client;

    /**
     * Creates a RealTimeDataReader that will connect to the given WebSocket URL.
     *
     * @param serverUrl the WebSocket server URL (e.g., "ws://localhost:8080")
     */
    public RealTimeDataReader(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * Connects to the WebSocket server and starts receiving data.
     * The connection is blocking — this method will wait until the connection is established.
     *
     * @param storage the DataStorage where received data will be stored
     */
    @Override
    public void readData(DataStorage storage) {
        try {
            client = new DataWebSocketClient(serverUrl, storage);
            client.connectBlocking();
        } catch (Exception e) {
            System.err.println("Failed to connect to WebSocket server: " + e.getMessage());
        }
    }

    /**
     * Closes the WebSocket connection if it is open.
     */
    public void stopReading() {
        if (client != null) {
            client.close();
        }
    }
}