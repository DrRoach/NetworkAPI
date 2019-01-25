package main.java.com.gitlab.networkapi;

import java.util.concurrent.TimeUnit;

public class DevClient extends Client {
    // Simplest constructor implementation which would be used by most people
    //public DevClient(String host, int port) {
    //    super(host, port);
    //}

    public DevClient(String host, int port, int timeout) {
        super(host, port, timeout);
    }

    public static void main(String[] args) {
        DevClient client = new DevClient("localhost", 2103, 1000);

        try {
            TimeUnit.SECONDS.sleep(5);
            client.send("CLIENT");
        } catch (Exception ex) {}
    }

    @Override
    public void messageReceived(String message) {
        System.out.println(message);
    }
}
