package NetworkAPI.Tests;

import NetworkAPI.DevClient;
import NetworkAPI.Exceptions.ClientConnectionFailedException;
import NetworkAPI.Exceptions.PortOutOfRangeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTests {
    @Test
    public void ConnectionTest() {
        DevClient client = new DevClient("127.0.0.1", 2103, 5000);

        assertTrue(client.connected());
    }

    @Test
    public void PortOutOfRangeTest() {
        assertThrows(PortOutOfRangeException.class,
            ()->{
                DevClient client = new DevClient("127.0.0.1", -4, 1000);
            });


        assertThrows(PortOutOfRangeException.class,
            ()->{
                DevClient client = new DevClient("127.0.0.1", 699999, 1000);
            });

        DevClient client = new DevClient("127.0.0.1", 2103, 1000);
        assertTrue(client.connected());
    }

    @Test
    public void ServerClosedTest() {
        assertThrows(ClientConnectionFailedException.class,
            ()->{
                DevClient client = new DevClient("123.123.123.123", 1111, 1000);
            });
    }
}
