package NetworkAPI;

import java.util.concurrent.TimeUnit;

public class DevClient extends Client {
    DevClient(String host, int port) {
        super(host, port);
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 2103);

        // Wait 5 seconds then message our server saying "HELLO SERVER"
        try {
            TimeUnit.SECONDS.sleep(5);

            System.out.println("Messaging server");
            client.send("HELLO SERVER");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void messageReceived(String message) {
        System.out.println(message);
    }
}
