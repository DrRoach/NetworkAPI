package com.gitlab.networkapi.encryption;

import com.gitlab.networkapi.encryption.setup.Setup;
import com.gitlab.networkapi.exceptions.ExceptionCodes;
import com.gitlab.networkapi.exceptions.FileNotFoundException;
import com.gitlab.networkapi.FileHandler;

import java.io.*;
import java.security.*;

public class KeyHandler {
    public static Key readPublic(Setup.KEY_TYPE keyType, String keyFile) {
        Key key = null;

        String keyDir;
        if (keyType == Setup.KEY_TYPE.SERVER) {
            keyDir = "server/";
        } else {
            keyDir = "client/";
        }

        try {
            FileHandler file = new FileHandler(keyDir + keyFile + ".pub");

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getName()));

            key = (Key) in.readObject();

            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.FILE_NOT_FOUND);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.IO_EXCEPTION);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.CLASS_NOT_FOUND);
        }

        return key;
    }

    public static Key readPrivate(String keyFile) {
        Key key = null;

        try {
            FileHandler file = new FileHandler(keyFile + ".prv");

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getName()));

            // Read the key from file
            key = (PrivateKey) in.readObject();

            in.close();
        } catch (java.io.FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.PRIVATE_KEY_NOT_FOUND);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.IO_EXCEPTION);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.CLASS_NOT_FOUND);
        }

        return key;
    }

    public static void generateKeyPair(Setup.KEY_TYPE keyType, String keyFile) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);

            KeyPair keyPair = generator.generateKeyPair();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(keyFile + ".pub"));
            out.writeObject(keyPair.getPublic());
            out.close();

            System.out.println("GENREATING");

            out = new ObjectOutputStream(new FileOutputStream("client/" + keyFile + ".prv"));
            out.writeObject(keyPair.getPrivate());
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.NO_SUCH_ALGORITHM);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.SERVER_PUBLIC_KEY_FILE_NOT_FOUND_EXCEPTION);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.SERVER_PUBLIC_KEY_IO_EXCEPTION);
        }
    }
}
