package main.java.com.github.networkapi;

import main.java.com.github.networkapi.commands.GetPublic;
import main.java.com.github.networkapi.encryption.Encrypt;
import main.java.com.github.networkapi.encryption.KeyHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {
    // Variable to control whether we should be listening for connections
    private boolean running;
    private ServerSocket server;
    private List<Connection> connections = new ArrayList<Connection>();
    private Server callbacks;
    private boolean useEncryption;

    ConnectionHandler(ServerSocket server, boolean useEncryption) {
        running = true;
        this.server = server;
        this.useEncryption = useEncryption;
    }

    public void run() {
        while (running) {
            try {
                Socket client = server.accept();

                Connection conn = new Connection(connections.size(), client, useEncryption);

                conn.setCallbacks(this);

                Thread connectionThread = new Thread(conn);
                connectionThread.start();

                connections.add(conn);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public boolean broadcast(String message) {
        for (Connection conn : connections) {
            conn.send(message);
        }

        return true;
    }

    public void broadcast(byte[] message) {
        for (Connection conn : connections) {
            conn.send(message);
        }
    }

    public void messageReceived(int connectionId, String message) {
        if (useEncryption) {
            Key serverPrivateKey = KeyHandler.readPrivate("key", KeyHandler.Type.Server);
            Encrypt encrypt = new Encrypt(serverPrivateKey);
            byte[] decryptedMessage = encrypt.decrypt(message.getBytes());

            String decryptedString = new String(decryptedMessage);

            boolean command = decryptedString.substring(0, 1).equals("!");

            if (command) {
                handleCommand(connectionId, decryptedString.substring(1));
                return;
            }

            callbacks.messageReceived(connections.get(connectionId), decryptedString);
        } else {
            callbacks.messageReceived(connections.get(connectionId), message);
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

    private void handleCommand(int connectionId, String command)
    {
        System.out.println(connectionId);
        System.out.println(command);
        String commandSplit[] = command.split(":");
        switch (commandSplit[0]) {
            case "get_public":
                Connection connectionToFetchKey = connections.get(Integer.parseInt(commandSplit[1]));

                connections.get(connectionId).send(connectionToFetchKey.getPublicKey());
                break;
        }
    }
}
