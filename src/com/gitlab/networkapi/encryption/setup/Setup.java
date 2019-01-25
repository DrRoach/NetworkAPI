package com.gitlab.networkapi.encryption.setup;

import com.gitlab.networkapi.config.Config;
import com.gitlab.networkapi.encryption.KeyHandler;
import com.gitlab.networkapi.encryption.Signature;

import java.io.*;
import java.security.Key;
import java.util.Set;

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

        if (!Setup.keyDirExists(type)) {
            Setup.createKeyDir(type);
        }

        if (!Setup.keysExist(type)) {
            System.out.println("Generating keys...");
            KeyHandler.generateKeyPair(keyType, keyFile);
        }

        Key privateKey = KeyHandler.readPrivate(type, keyFile);

        // Generate our signature to be passed to clients
        signature = Signature.generateSignature(privateKey, Config.encryptionSignature);
    }

    public static boolean keysExist(KEY_TYPE type) {
        return Setup.publicKeyExists(type) && Setup.privateKeyExists(type);
    }

    /**
     * Method to get fully generated signature from the server
     * @return
     */
    public byte[] getSignature() {
        return signature;
    }

    public static boolean deleteKeys() {
        return Setup.deletePublicKey() && Setup.deletePrivateKey();
    }

    private static boolean publicKeyExists(KEY_TYPE type) {
        File publicKey = new File(type + keyFile + ".pub");

        return publicKey.isFile();
    }

    private static boolean privateKeyExists(KEY_TYPE type) {
        File privateKey = new File(type + keyFile + ".prv");

        return privateKey.isFile();
    }

    private static boolean deletePublicKey() {
        File publicKey = new File(keyFile + ".pub");
        return publicKey.delete();
    }

    private static boolean deletePrivateKey() {
        File privateKey = new File(keyFile + ".prv");
        return privateKey.delete();
    }

    private static boolean createKeyDir(KEY_TYPE type)
    {
        String dir;
        if (type == KEY_TYPE.SERVER) {
            dir = "server/";
        } else {
            dir = "client/";
        }
        return new File(dir).mkdirs();
    }

    private static boolean keyDirExists(KEY_TYPE type)
    {
        String dir;
        if (type == KEY_TYPE.SERVER) {
            dir = "server/";
        } else {
            dir = "client/";
        }

        return new File(dir).exists();
    }
}
