package main.java.com.github.networkapi.communicators;

import main.java.com.github.networkapi.Connection;

import java.io.DataInputStream;
import java.io.IOException;

public class Receive implements Runnable {
    private DataInputStream in;
    private Connection connection;
    private boolean running = true;

    public Receive(DataInputStream in) {
        this.in = in;
    }

    public void run() {
        try {
            int read;
            StringBuilder returnString = new StringBuilder();
            while (running) {
                // Read our byte array until we find a null character
                while ((read = in.readByte()) > -1) {
                    returnString.append((char) read);
                }

                // If the returnString has something in it then notify connection and then
                //  reset the StringBuilder
                if (returnString.length() > 0) {
                    System.out.println(returnString.toString());
                    connection.messageReceived(returnString.toString());
                    returnString.setLength(0);
                }
            }
        } catch (IOException ex) {
            // If we hit here assume that connection has been lost
            connection.connectionClosed();
            running = false;
        }
    }

    public void setCallback(Connection connection) {
        this.connection = connection;
    }
}
