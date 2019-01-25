package main.java.com.gitlab.networkapi;

import java.util.concurrent.TimeUnit;

public class DevHost extends Server {
    static DevHost host;

    public DevHost(int port) {
        super(port);
    }

    public static void main(String[] args) {
        host = new DevHost(2103);
    }

    @Override
    public void newConnection(Connection connection) {
        super.newConnection(connection);

        connection.send("Hello new client");

        try {
            TimeUnit.SECONDS.sleep(5);
            host.broadcast("BROAD");
        } catch (Exception ex) {}
    }

    @Override
    public void messageReceived(String message) {
        System.out.println(message);
    }

    @Override
    public void connectionClosed(int id) {
        System.out.println("The connection " + id + " has been closed");
    }
}
