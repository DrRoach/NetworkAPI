package main.java.com.github.networkapi;

import main.java.com.github.networkapi.communicators.Receive;
import main.java.com.github.networkapi.communicators.Send;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements Runnable {
    private Socket conn;
    private Send send;
    private ConnectionHandler serverCallbacks = null;
    private Client clientCallbacks = null;
    private int id = -1;

    Connection(Socket conn) {
        this.conn = conn;
    }

    Connection(int id, Socket conn) {
        this.id = id;
        this.conn = conn;
    }

    public void run() {
        try {
            // Create streams
            DataInputStream in = new DataInputStream(conn.getInputStream());
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            Receive receive = new Receive(in);
            receive.setCallback(this);

            Thread receiveThread = new Thread(receive);
            receiveThread.start();

            // Create send object to be used to send messages
            send = new Send(out);
            Thread sendThread = new Thread(this.send);
            sendThread.start();

            // If instance of Connection object is a Server then call `newConnection()`
            if (serverCallbacks != null) {
                serverCallbacks.newConnection(this);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void send(String message) {
        send.setMessage(message);
        send.send();
    }

    public void send(byte[] message) {
        send.setMessage(message);
        send.send();
    }

    public InetAddress getAddress() {
        return conn.getInetAddress();
    }

    public void messageReceived(String message) {
        if (serverCallbacks != null) {
            serverCallbacks.messageReceived(message);
        } else {
            clientCallbacks.messageReceived(message);
        }
    }

    public void setCallbacks(ConnectionHandler handler) {
        serverCallbacks = handler;
    }

    public void setCallbacks(Client client) {
        clientCallbacks = client;
    }

    public void connectionClosed() {
        if (serverCallbacks != null) {
            serverCallbacks.connectionClosed(id);
        } else {
            clientCallbacks.connectionClosed(id);
        }
    }
}
