package NetworkAPI;

import NetworkAPI.Communicators.Receive;
import NetworkAPI.Communicators.Send;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements Runnable {
    private Socket _conn;
    private Send _send;
    private ConnectionHandler _serverCallbacks = null;
    private Client _clientCallbacks = null;
    private int _id = -1;

    Connection(Socket conn) {
        _conn = conn;
    }

    Connection(int id, Socket conn) {
        _id = id;
        _conn = conn;
    }

    public void run() {
        try {
            // Create streams
            DataInputStream _in = new DataInputStream(_conn.getInputStream());
            DataOutputStream _out = new DataOutputStream(_conn.getOutputStream());

            Receive _receive = new Receive(_in);
            _receive.setCallback(this);

            Thread receiveThread = new Thread(_receive);
            receiveThread.start();

            // Create our send/receive objects
            _send = new Send(_out);
            if (_serverCallbacks != null) {
                _serverCallbacks.newConnection(this);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void send(String message) {
        _send.setMessage(message);
        Thread sendThread = new Thread(_send);
        sendThread.start();
    }

    public InetAddress getAddress() {
        return _conn.getInetAddress();
    }

    public void messageReceived(String message) {
        if (_serverCallbacks != null) {
            _serverCallbacks.messageReceived(message);
        } else {
            _clientCallbacks.messageReceived(message);
        }
    }

    public void setCallbacks(ConnectionHandler handler) {
        _serverCallbacks = handler;
    }

    public void setCallbacks(Client client) {
        _clientCallbacks = client;
    }

    public void connectionClosed() {
        if (_serverCallbacks != null) {
            _serverCallbacks.connectionClosed(_id);
        } else {
            _clientCallbacks.connectionClosed(_id);
        }
    }
}
