package test.java;

import main.java.com.github.networkapi.DevHost;
import main.java.com.github.networkapi.exceptions.PortOutOfRangeException;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertThrows;

public class ServerTests {
    @Test
    public void PortRangeTest() {
        assertThrows(PortOutOfRangeException.class,
            ()->{
                DevHost host = new DevHost(-4);
            });

        assertThrows(PortOutOfRangeException.class,
            ()->{
                DevHost host = new DevHost(6999999);
            });

        DevHost host = new DevHost(2103);
        assertTrue(host.online());
        host.stop();
    }

    @Test
    public void ConnectionTest() {
        DevHost host = new DevHost(2103);

        assertTrue(host.online());
        host.stop();
    }
}
