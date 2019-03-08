package test;

import main.java.com.github.networkapi.Connection;
import main.java.com.github.networkapi.Server;
import main.java.com.github.networkapi.exceptions.PortOutOfRangeException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;

import java.net.Socket;

import static org.junit.Assert.*;

public class ServerTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void testPortOutOfRange()
    {
        try {
            new Server(-1);
            fail("Expected port out of range exception");
        } catch (PortOutOfRangeException ex) {
        }

        try {
            new Server(65536);
            fail("Expected port out of range exception");
        } catch (PortOutOfRangeException ex) {
        }
    }

    @Test
    public void testStartServer()
    {
        systemOutRule.clearLog();
        Server server = new Server(2103);
        assertEquals("Starting server...\nServer up and running.\n", systemOutRule.getLog());

        server.stop();
    }

    @Test
    public void testBroadcast()
    {
        Server server = new Server(2103);
        assertTrue(server.broadcast("TESTING"));

        server.stop();
    }

    @Test
    public void testNewConnection()
    {
        Server server = new Server(2103, false);
        Connection connection = new Connection(new Socket());

        systemOutRule.clearLog();
        server.newConnection(connection);
        assertEquals("New connection from: null\n", systemOutRule.getLog());

        server.stop();
    }

    @Test
    public void testConnectionClosed()
    {
        Server server = new Server(2103);

        systemOutRule.clearLog();
        server.connectionClosed(12);

        assertEquals("Connection 12 closed\n", systemOutRule.getLog());

        server.stop();
    }

    @Test
    public void testOnline()
    {
        Server server = new Server(2103);
        assertTrue(server.online());

        server.stop();
        assertFalse(server.online());
    }
}
