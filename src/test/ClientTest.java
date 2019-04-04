package test;

import main.java.com.github.networkapi.Client;
import main.java.com.github.networkapi.Server;
import main.java.com.github.networkapi.exceptions.ClientConnectionFailedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class ClientTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test(expected = ClientConnectionFailedException.class)
    public void testServerClosed()
    {
        Client client = new Client("127.0.0.1", 2103);
    }

    @Test
    public void testConnect()
    {
        Server server = new Server(2103);

        systemOutRule.clearLog();
        Client client = new Client("127.0.0.1", 2103);

        assertEquals("Connected\n", systemOutRule.getLog());

        server.stop();
    }

    @Test
    public void testConnectionClosed()
    {
        Server server = new Server(2103);
        Client client = new Client("127.0.0.1", 2103);

        systemOutRule.clearLog();

        client.connectionClosed(0);

        assertEquals("Lost connection to server\n", systemOutRule.getLog());

        server.stop();
    }

    @Test
    public void testConnected()
    {
        Server server = new Server(2103);

        Client client = new Client("127.0.0.1", 2103);

        server.stop();

        assertFalse(client.connected());
    }

    @Test
    public void testGetAddress()
    {
        Server server = new Server(2103);

        Client client = new Client("127.0.0.1", 2103);

        assertEquals("/127.0.0.1", client.getAddress().toString());

        server.stop();
    }
}
