package test.java;

import main.java.com.github.networkapi.encryption.ServerSetup;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

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
