package NetworkAPI.Encryption;

import NetworkAPI.Exceptions.ExceptionCodes;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class ServerSetup {
    public static void generateKeypair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.NO_SUCH_ALGORITHM);
        }
    }
}
