package main.java.com.github.networkapi.encryption.setup;

import main.java.com.github.networkapi.config.Config;
import main.java.com.github.networkapi.encryption.KeyHandler;
import main.java.com.github.networkapi.encryption.Signature;

import java.io.*;
import java.security.Key;
import java.util.Set;

public class Setup {
    // The signature used to validate the server to new connections
    private byte[] signature;

    public Setup() {
        if (!Setup.keysExist()) {
            System.out.println("Generating server keys...");
            KeyHandler.generateKeyPair();
        }

        Key privateKey = KeyHandler.readPrivate("server");

        // Generate our signature to be passed to clients
        signature = Signature.generateSignature(privateKey, Config.encryptionSignature);
    }

    public static boolean keysExist() {
        return Setup.publicKeyExists() && Setup.privateKeyExists();
    }

    /**
     * Method to get fully generated signature from the server
     * @return
     */
    public byte[] getSignature() {
        return signature;
    }

    private static boolean publicKeyExists() {
        File publicKey = new File("server.pub");

        return publicKey.isFile();
    }

    private static boolean privateKeyExists() {
        File privateKey = new File("server.prv");

        return privateKey.isFile();
    }

    public static boolean deleteKeys() {
        return Setup.deletePublicKey() && Setup.deletePrivateKey();
    }

    private static boolean deletePublicKey() {
        File publicKey = new File("server.pub");
        return publicKey.delete();
    }

    private static boolean deletePrivateKey() {
        File privateKey = new File("server.prv");
        return privateKey.delete();
    }
}
