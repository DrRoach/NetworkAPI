package NetworkAPI;

import NetworkAPI.Exceptions.ClientConnectionFailedException;
import NetworkAPI.Exceptions.PortOutOfRangeException;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Connection _connection;

    public Client(String host, int port) {
        // Make sure that our port is in range
        if (port < 0 || port > 65535) {
            throw new PortOutOfRangeException("You can only use port 0-65535");
        }

        try {
            // Connect to our server
            Socket client = new Socket(host, port);

            // Handle our client/server interactions
            handle(client);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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

    public boolean connected() {
        if (_connection == null) {
            return false;
        } else {
            return true;
        }
    }
}
