package NetworkAPI;

import java.util.concurrent.TimeUnit;

public class DevHost extends Server {
    DevHost(int port) {
        super(port);
    }

    public static void main(String[] args) {
        DevHost host = new DevHost(2103);

        try {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("BROADCASTING");
            host.broadcast("TESTING BROAD");
        } catch (Exception ex) {}
    }

    @Override
    public void newConnection(Connection connection) {
        super.newConnection(connection);

        connection.send("Hello new client");
    }

    @Override
    public void messageReceived(String message) {
        super.messageReceived(message);

        System.out.println("YAY: " + message);
    }

    @Override
    public void connectionClosed(int id) {
        System.out.println("The connection " + id + " has been closed");
    }
}
