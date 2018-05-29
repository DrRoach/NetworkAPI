package NetworkAPI.Communicators;

import NetworkAPI.Connection;

import java.io.DataInputStream;
import java.io.IOException;

public class Receive implements Runnable {
    private DataInputStream _in;
    private Connection _connection;
    private boolean _running = true;

    public Receive(DataInputStream in) {
        _in = in;
    }

    public void run() {
        try {
            while (_running) {
                _connection.messageReceived(_in.readUTF());
            }
        } catch (IOException ex) {
            // If we hit here assume that connection has been lost
            _connection.connectionClosed();
            _running = false;
        }
    }

    public void setCallback(Connection connection) {
        _connection = connection;
    }
}
