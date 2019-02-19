package main.java.com.github.networkapi.encryption;

import main.java.com.github.networkapi.exceptions.ExceptionCodes;
import main.java.com.github.networkapi.exceptions.FileNotFoundException;
import main.java.com.github.networkapi.FileHandler;

import java.io.*;
import java.security.*;

public class KeyHandler {
    public enum Type {
        Client,
        Server
    }

    public static Key readPublic(String keyFile, Type type) {
        Key key = null;

        String keyDir = getKeyDir(type);

        try {
            FileHandler file = new FileHandler(keyDir + keyFile + ".pub");

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));

            key = (Key) in.readObject();

            in.close();
        } catch (main.java.com.github.networkapi.exceptions.FileNotFoundException ex) {
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

    public static Key readPrivate(String keyFile, Type type) {
        Key key = null;

        String keyDir = getKeyDir(type);

        try {
            FileHandler file = new FileHandler(keyDir + keyFile + ".prv");

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));

            // Read the key from file
            key = (PrivateKey) in.readObject();

            in.close();
        } catch (java.io.FileNotFoundException ex) {
            System.out.println(keyDir + keyFile + ".prv");
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

    public static void generateKeyPair(Type type) {
        String keyDir = getKeyDir(type);

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);

            KeyPair keyPair = generator.generateKeyPair();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(keyDir + "key.pub"));
            out.writeObject(keyPair.getPublic());
            out.close();

            out = new ObjectOutputStream(new FileOutputStream(keyDir + "key.prv"));
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

    private static String getKeyDir(Type type)
    {
        if (type == Type.Client) {
            return "./client/";
        } else {
            return "./server/";
        }
    }
}
