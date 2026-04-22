package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Sends patient data to all connected WebSocket clients.
 * Uses the Java-WebSocket library to create a simple WebSocket server.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    /**
     * Creates a WebSocket server on the specified port and starts it.
     *
     * @param port the port number to listen on
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    /**
     * Sends a single data entry to all connected WebSocket clients.
     * The message is formatted as: patientId, timestamp, label, data
     *
     * @param patientId the ID of the patient
     * @param timestamp the time when the data was generated
     * @param label the type of data
     * @param data the actual data value
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    /**
     * Internal WebSocket server implementation.
     * Handles client connections, disconnections and errors.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {

        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
