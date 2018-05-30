package NetworkAPI;

import NetworkAPI.Exceptions.ClientConnectionFailedException;
import NetworkAPI.Exceptions.PortOutOfRangeException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    private Connection _connection;

    /**
     * Constructor that is called when user wants to use default timeout.
     *
     * This constructor should be called when the user is happy to accept 5 seconds
     *  as the default timeout for connection. This is the simplest method to implement
     *  when using this library but may not be useful in all cases such as slow
     *  connection with server.
     *
     * @param host - The server IP address to connect to
     * @param port - The server port to connect to
     */
    public Client(String host, int port) {
        new Client(host, port, 5000);
    }

    public Client(String host, int port, int timeout) {
        // Make sure that our port is in range
        if (port < 0 || port > 65535) {
            throw new PortOutOfRangeException("You can only use port 0-65535");
        }

        try {
            // Connect to our server
            // Create the socket address obj for our server
            SocketAddress serverAddress = new InetSocketAddress(host, port);
            Socket client = new Socket();
            // Try and connect to our server socket
            client.connect(serverAddress, timeout);

            // Handle our client/server interactions
            handle(client);
        } catch (IOException ex) {
            throw new ClientConnectionFailedException("There was an error when connecting to the server: " + ex.getMessage());
        }
    }

    private void handle(Socket client) {
        // Start our outgoing/incoming threads
        _connection = new Connection(client);

        _connection.setCallbacks(this);

        Thread connectionThread = new Thread(_connection);
        connectionThread.start();
    }

    public void messageReceived(String message) {
    }

    public void connectionClosed(int id) {
        System.out.println("Lost connection to server");
        System.exit(1);
    }

    public void send(String message) {
        _connection.send(message);
    }

    public InetAddress getAddress() {
        return _connection.getAddress();
    }

    public boolean connected() {
        return _connection != null;
    }
}
