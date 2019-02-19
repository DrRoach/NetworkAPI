package main.java.com.github.networkapi;

import main.java.com.github.networkapi.encryption.Encrypt;
import main.java.com.github.networkapi.encryption.KeyHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {
    // Variable to control whether we should be listening for connections
    private boolean running;
    private ServerSocket server;
    private List<Connection> connections = new ArrayList<Connection>();
    private Server callbacks;
    private byte[] signature;
    private boolean useEncryption = true;

    ConnectionHandler(ServerSocket server) {
        running = true;
        this.server = server;
        this.useEncryption = false;
    }

    ConnectionHandler(ServerSocket server, byte[] signature) {
        running = true;
        this.server = server;
        this.signature = signature;
    }

    public void run() {
        while (running) {
            try {
                Socket client = server.accept();

                Connection conn = new Connection(connections.size(), client);

                conn.setCallbacks(this);

                Thread connectionThread = new Thread(conn);
                connectionThread.start();

                connections.add(conn);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void broadcast(String message) {
        for (Connection conn : connections) {
            conn.send(message);
        }
    }

    public void broadcast(byte[] message) {
        for (Connection conn : connections) {
            conn.send(message);
        }
    }

    public void messageReceived(String message) {
        if (useEncryption) {
            Key serverPrivateKey = KeyHandler.readPrivate("key", KeyHandler.Type.Server);
            Encrypt encrypt = new Encrypt(serverPrivateKey);
            byte[] decryptedMessage = encrypt.decrypt(message.getBytes());

            String decryptedString = new String(decryptedMessage);

            callbacks.messageReceived(decryptedString);
        } else {
            callbacks.messageReceived(message);
        }
    }

    public void connectionClosed(int id) {
        connections.set(id, null);

        callbacks.connectionClosed(id);
    }

    public void setCallbacks(Server callbacks) {
        this.callbacks = callbacks;
    }

    public void newConnection(Connection connection) {
        this.callbacks.newConnection(connection);
    }

    public void stop() {
        running = false;
    }
}
