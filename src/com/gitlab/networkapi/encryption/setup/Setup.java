package com.gitlab.networkapi.encryption.setup;

import com.gitlab.networkapi.config.Config;
import com.gitlab.networkapi.encryption.KeyHandler;
import com.gitlab.networkapi.encryption.Signature;

import java.io.*;
import java.security.Key;

public class Setup {
    public static String keyFile = "networkapi";
    public enum KEY_TYPE {
        SERVER,
        CLIENT
    }

    private KEY_TYPE keyType;
    private byte[] signature;

    public Setup(KEY_TYPE type) {
        this.keyType = type;

        if (!Setup.keysExist()) {
            System.out.println("Generating keys...");
            KeyHandler.generateKeyPair(keyType, keyFile);
        }

        Key privateKey = KeyHandler.readPrivate(keyFile);

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
        File publicKey = new File(keyFile + ".pub");

        return publicKey.isFile();
    }

    private static boolean privateKeyExists() {
        File privateKey = new File(keyFile + ".prv");

        return privateKey.isFile();
    }

    public static boolean deleteKeys() {
        return Setup.deletePublicKey() && Setup.deletePrivateKey();
    }

    private static boolean deletePublicKey() {
        File publicKey = new File(keyFile + ".pub");
        return publicKey.delete();
    }

    private static boolean deletePrivateKey() {
        File privateKey = new File(keyFile + ".prv");
        return privateKey.delete();
    }
}
