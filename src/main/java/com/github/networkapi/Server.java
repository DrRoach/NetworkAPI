package main.java.com.github.networkapi;

import main.java.com.github.networkapi.encryption.ServerSetup;
import main.java.com.github.networkapi.exceptions.PortOutOfRangeException;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private ConnectionHandler connectionHandler;
    private ServerSocket server;
    private boolean useEncryption;
    private byte[] signature;

    /**
     * Start new server using encryption by default
     *
     * @param port - Port number to start server on
     */
    public Server(int port) {
        // Make sure that we use encryption by default
        startServer(port, true);
    }

    /**
     * Start new server but allow user to decide whether or not to use encryption
     *
     * This method starts our server but it allows the user to disable encryption and server signature verification if
     *  they don't want to use it. Whilst this isn't recommended as it allows people to eavesdrop on the messages being
     *  sent or to pose as the server.
     *
     * @param port - Port number to host the server on
     * @param useEncryption - Boolean to enable/disable message encryption and server signature verification
     */
    public Server(int port, boolean useEncryption) {
        startServer(port, useEncryption);
    }

    private boolean portInRange(int port) {
        // Make sure that our port doesn't exceed valid range
        return port >= 0 && port <= 65535;
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
    private void startServer(int port, boolean useEncryption) {
        this.useEncryption = useEncryption;

        if (useEncryption) {
            // Setup everything that's needed for encryption
            setupEncryption();
        }

        System.out.println("Starting server...");

        if (!portInRange(port)) {
            throw new PortOutOfRangeException("You can only use ports 0-65535");
        }

        try {
            // Open our server socket
            server = new ServerSocket(port);

            // Start listening for new connections to our server
            ConnectionHandler connectionHandler;

            // If we are using encryption we need to make sure to pass our signature
            if (useEncryption) {
                connectionHandler = new ConnectionHandler(server, signature);
            } else {
                connectionHandler = new ConnectionHandler(server);
            }

            Thread connectionThread = new Thread(connectionHandler);
            connectionThread.start();

            // Store our ConnectionHandler to be used elsewhere
            this.connectionHandler = connectionHandler;

            // Set our callbacks method in ConnectionHandler to reference this class
            connectionHandler.setCallbacks(this);

            System.out.println("Server up and running.");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setupEncryption() {
        // Make sure that the server is fully setup before use
        ServerSetup serverSetup = new ServerSetup();

        // Set the server signature to be passed to clients
        signature = serverSetup.getSignature();
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
        connectionHandler.broadcast(message);
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
        if (useEncryption) {
            // Send server signature to the new connection
            //TODO: FIX THIS
            byte[] signature = null;
        }

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
        return connectionHandler != null;
    }

    public void stop() {
        try {
            // Let our connection handler know that we're stopping
            connectionHandler.stop();

            // Close our server socket
            server.close();
        } catch (IOException ex) {
            System.out.println("Error when stopping server.");
            System.out.println(ex.getMessage());
        }
    }
}
