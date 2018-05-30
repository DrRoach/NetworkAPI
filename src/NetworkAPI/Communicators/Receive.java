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
            int read;
            StringBuilder returnString = new StringBuilder();
            while (_running) {
                // Read our byte array until we find a null character
                while ((read = _in.readByte()) > -1) {
                    returnString.append((char) read);
                }

                // If the returnString has something in it then notify connection and then
                //  reset the StringBuilder
                if (returnString.length() > 0) {
                    _connection.messageReceived(returnString.toString());
                    returnString.setLength(0);
                }
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
