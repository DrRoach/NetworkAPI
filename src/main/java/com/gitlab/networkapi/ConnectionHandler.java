package main.java.com.gitlab.networkapi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler implements Runnable {
    // Variable to control whether we should be listening for connections
    private boolean _running;
    private ServerSocket _server;
    private List<Connection> _connections = new ArrayList<Connection>();
    private Server _callbacks;

    ConnectionHandler(ServerSocket server) {
        _running = true;
        _server = server;
    }

    public void run() {
        while (_running) {
            try {
                Socket client = _server.accept();

                Connection conn = new Connection(_connections.size(), client);

                conn.setCallbacks(this);

                Thread connectionThread = new Thread(conn);
                connectionThread.start();

                _connections.add(conn);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void broadcast(String message) {
        for (Connection conn : _connections) {
            conn.send(message);
        }
    }

    public void broadcast(byte[] message) {
        for (Connection conn : _connections) {
            conn.send(message);
        }
    }

    public void messageReceived(String message) {
        _callbacks.messageReceived(message);
    }

    public void connectionClosed(int id) {
        _connections.set(id, null);

        _callbacks.connectionClosed(id);
    }

    public void setCallbacks(Server callbacks) {
        this._callbacks = callbacks;
    }

    public void newConnection(Connection connection) {
        this._callbacks.newConnection(connection);
    }

    public void stop() {
        _running = false;
    }
}
