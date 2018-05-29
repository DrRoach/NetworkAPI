package NetworkAPI;

import java.util.concurrent.TimeUnit;

public class DevClient {
    public static void main(String[] args) {
        Client client = new Client("localhost", 2103);

        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("Messaging server");
            client.send("HELLOW SERVER");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
