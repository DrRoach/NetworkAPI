package test.java;

import main.java.com.github.networkapi.DevClient;
import main.java.com.github.networkapi.exceptions.ClientConnectionFailedException;
import main.java.com.github.networkapi.exceptions.PortOutOfRangeException;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertThrows;

public class ClientTests {
    @Test
    public void ConnectionTest() {
        DevClient client = new DevClient("127.0.0.1", 2103, 5000);

        assertTrue(client.connected());
    }

    @Test
    public void PortRangeTest() {
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
