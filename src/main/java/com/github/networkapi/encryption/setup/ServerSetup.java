package main.java.com.github.networkapi.encryption.setup;

import main.java.com.github.networkapi.config.Config;
import main.java.com.github.networkapi.encryption.KeyHandler;
import main.java.com.github.networkapi.encryption.Signature;

import java.io.*;
import java.security.Key;

public class ServerSetup {
    // The signature used to validate the server to new connections
    private byte[] signature;

    public ServerSetup() {
        if (!ServerSetup.keysExist()) {
            System.out.println("Generating server keys...");
            KeyHandler.generateKeyPair();
        }

        Key privateKey = KeyHandler.readPrivate("server");

        // Generate our signature to be passed to clients
        signature = Signature.generateSignature(privateKey, Config.encryptionSignature);
    }

    public static boolean keysExist() {
        return ServerSetup.publicKeyExists() && ServerSetup.privateKeyExists();
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
        return ServerSetup.deletePublicKey() && ServerSetup.deletePrivateKey();
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
