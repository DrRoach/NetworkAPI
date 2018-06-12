package main.java.com.github.networkapi.encryption;

import java.io.*;

public class ServerSetup {
    public ServerSetup() {
        if (!ServerSetup.keysExist()) {
            System.out.println("Generating server keys...");
            KeyHandler.generateKeyPair();
        }
    }

    private static boolean keysExist() {
        return ServerSetup.publicKeyExists() && ServerSetup.privateKeyExists();
    }

    private static boolean publicKeyExists() {
        File publicKey = new File("server.pub");

        return publicKey.isFile();
    }

    private static boolean privateKeyExists() {
        File privateKey = new File("server.prv");

        return privateKey.isFile();
    }
}
