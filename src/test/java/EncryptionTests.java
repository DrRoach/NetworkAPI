package test.java;

import main.java.com.github.networkapi.encryption.setup.Setup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncryptionTests {
    @Test
    public void keyCreationTest() {
        assertTrue(Setup.deleteKeys());

        // Make sure keys are deleted
        assertTrue(!Setup.keysExist());

        Setup setup = new Setup();

        assertTrue(Setup.keysExist());
    }
}
