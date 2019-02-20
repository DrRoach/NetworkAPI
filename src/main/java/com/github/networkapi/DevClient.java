package main.java.com.github.networkapi;

import java.util.concurrent.TimeUnit;

public class DevClient extends Client {
    // Simplest constructor implementation which would be used by most people
    //public DevClient(String host, int port) {
    //    super(host, port);
    //}
    DevClient client;

    public DevClient(String host, int port, int timeout) {
        super(host, port, timeout);

        client = this;
    }

    public static void main(String[] args) {
        new DevClient("localhost", 2103, 1000);
    }

    public void messageReceived(String message) {
        super.messageReceived(message);

        message = getMessage();

        // Message will be `null` on the first message received whilst using encryption.
        //  This is because the
        if (message == null) {
            return;
        }

        System.out.println(message);

        client.send("HI");
    }

    public void connectedToServer()
    {
        super.connectedToServer();

        System.out.println("CONNECTED TO SERVER");
    }
}
