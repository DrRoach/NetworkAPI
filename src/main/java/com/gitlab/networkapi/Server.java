package main.java.com.gitlab.networkapi;

import main.java.com.gitlab.networkapi.Exceptions.PortOutOfRangeException;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private ConnectionHandler connectionHandler;
    private ServerSocket server;

    public Server(int port) {
        startServer(port, true);
    }

    public Server(int port, boolean useEncryption) {
        startServer(port, useEncryption);
    }

    /**
     * First method called to make a new Server instance
     *
     * This method creates the ServerSocket to open for connections. It then creates the new ConnectionHandler
     *  and starts a thread to handle all connections. This ConnectionHandler is what interacts with all Connections
     *  rather than this Server class. This is a simple abstraction that calls methods in the ConnectionHandler
     *  class.
     *
     * @param port Port number to host the Server on
     */
    public void startServer(int port, boolean useEncryption) {
        // Make sure that our port doesn't exceed valid range
        if (port < 0 || port > 65535) {
            throw new PortOutOfRangeException("You can only use ports 0-65535");
        }

        try {
            // Open our server socket
            this.server = new ServerSocket(port);

            // Start listening for new connections to our server
            ConnectionHandler connectionHandler = new ConnectionHandler(this.server);
            Thread connectionThread = new Thread(connectionHandler);
            connectionThread.start();

            // Store our ConnectionHandler to be used elsewhere
            this.connectionHandler = connectionHandler;

            // Set our callbacks method in ConnectionHandler to reference this class
            this.connectionHandler.setCallbacks(this);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Send message to all connected clients
     *
     * This call is passed on to the ConnectionHandler which then attempts to send the `message` to all of the
     *  connected clients.
     *
     * @param message The message that will be sent to the connected clients
     */
    public void broadcast(String message) {
        this.connectionHandler.broadcast(message);
    }

    /**
     * Callback when a new connection is made to the server
     *
     * When a new client connects to the server. This is the method that will be called by the ConnectionHandler.
     *  This method is here so that it can be overwritten by the end user in order to do any processing they may
     *  wish to do.
     *
     * @param connection Socket connection for the client
     */
    public void newConnection(Connection connection) {
        System.out.println("New connection from: " + connection.getAddress());
    }

    /**
     * Callback when a new message is received
     *
     * When a new message is received from a client the message is propagated up to this method. When this method
     *  is overwritten it is important to call `super.messageReceived()` first to handle the heartbeat checks from
     *  the client.
     *
     * @param message Message received from the client
     */
    public void messageReceived(String message) {
    }

    public void connectionClosed(int id) {
        System.out.println("Connection " + id + " closed");
    }

    public boolean online() {
        return this.connectionHandler != null;
    }

    public void stop() {
        try {
            // Let our connection handler know that we're stopping
            this.connectionHandler.stop();

            // Close our server socket
            this.server.close();
        } catch (IOException ex) {
            System.out.println("Error when stopping server.");
            System.out.println(ex.getMessage());
        }
    }
}
