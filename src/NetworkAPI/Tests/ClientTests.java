package NetworkAPI.Tests;

import NetworkAPI.Client;
import NetworkAPI.Exceptions.ClientConnectionFailedException;
import NetworkAPI.Exceptions.PortOutOfRangeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTests {
    @Test
    public void ConnectionTest() {
        Client client = new Client("127.0.0.1", 2103);

        assertTrue(client.connected());
    }

    @Test
    public void PortOutOfRangeTest() {
        assertThrows(PortOutOfRangeException.class,
            ()->{
                Client client = new Client("127.0.0.1", -4);
            });


        assertThrows(PortOutOfRangeException.class,
            ()->{
                Client client = new Client("127.0.0.1", 699999);
            });

        Client client = new Client("127.0.0.1", 2103);
    }

    @Test
    public void ServerClosedTest() {
        assertThrows(ClientConnectionFailedException.class,
            ()->{
                Client client = new Client("123.123.123.123", 1111);
            });
    }
}
