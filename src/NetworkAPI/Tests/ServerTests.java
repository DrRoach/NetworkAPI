package NetworkAPI.Tests;

import NetworkAPI.Exceptions.PortOutOfRangeException;
import NetworkAPI.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerTests {
    @Test
    public void portOutOfRangeExceptionTest() {
        assertThrows(PortOutOfRangeException.class,
            ()->{
                Server server = new Server(-4);
            });

        assertThrows(PortOutOfRangeException.class,
            ()->{
                Server server = new Server(6999999);
            });

        Server server = new Server(2103);
    }
}
