package test.java;

import main.java.com.github.networkapi.encryption.setup.ServerSetup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncryptionTests {
    @Test
    public void keyCreationTest() {
        assertTrue(ServerSetup.deleteKeys());

        // Make sure keys are deleted
        assertTrue(!ServerSetup.keysExist());

        ServerSetup setup = new ServerSetup();

        assertTrue(ServerSetup.keysExist());
    }
}
