package main.java.com.github.networkapi.communicators;

import main.java.com.github.networkapi.Connection;

import javax.annotation.processing.SupportedSourceVersion;
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
            while (running) {
                connection.messageReceived(in.readUTF());
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
