package main.java.com.github.networkapi;

import main.java.com.github.networkapi.communicators.Receive;
import main.java.com.github.networkapi.communicators.Send;
import main.java.com.github.networkapi.encryption.Encrypt;
import main.java.com.github.networkapi.encryption.KeyHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;

public class Connection implements Runnable {
    private Socket conn;
    private Send send;
    private ConnectionHandler serverCallbacks = null;
    private Client clientCallbacks = null;
    private int id = -1;
    private Key connectionPublicKey;
    private boolean useEncryption;
    private boolean sentPublicKey = false;

    Connection(Socket conn) {
        this.conn = conn;
    }

    Connection(int id, Socket conn) {
        this.id = id;
        this.conn = conn;
        this.useEncryption = false;
    }

    Connection(int id, Socket conn, boolean useEncryption)
    {
        this.id = id;
        this.conn = conn;
        this.useEncryption = useEncryption;
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
            } else {
                clientCallbacks.connectedToServer();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void send(String message) {
        if (connectionPublicKey != null && sentPublicKey) {
            Encrypt encrypt = new Encrypt(connectionPublicKey);
            byte[] encryptedMessage = encrypt.encrypt(message);
            send.setMessage(encryptedMessage);
        } else {
            send.setMessage(message);
        }

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
            if (connectionPublicKey == null && useEncryption) {
                connectionPublicKey = KeyHandler.generateFromBytes(message.getBytes());

                // Send the server public key back
                Key publicKey = KeyHandler.readPublic("key", KeyHandler.Type.Server);
                try {
                    KeyFactory factory = KeyFactory.getInstance("RSA");
                    RSAPublicKeySpec pub = factory.getKeySpec(publicKey, RSAPublicKeySpec.class);

                    this.send(pub.getModulus() + ":" + pub.getPublicExponent());
                    sentPublicKey = true;
                } catch (Exception ex) {
                    System.out.println("FAILED TO SEND SERVER PUBLIC KEY");
                }
            } else {
                serverCallbacks.messageReceived(this.id, message);
            }
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

    public String getPublicKey()
    {
        Key publicKey = KeyHandler.readPublic("key", KeyHandler.Type.Server);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = factory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            return pub.getModulus() + ":" + pub.getPublicExponent();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "";
    }
}
