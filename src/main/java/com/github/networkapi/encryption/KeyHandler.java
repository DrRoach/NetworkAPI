package main.java.com.github.networkapi.encryption;

import main.java.com.github.networkapi.exceptions.ExceptionCodes;
import main.java.com.github.networkapi.exceptions.FileNotFoundException;
import main.java.com.github.networkapi.FileHandler;

import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyHandler {
    public static Key readPublic(String keyFile) {
        Key key = null;

        try {
            FileHandler file = new FileHandler(keyFile + ".pub");

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getName()));

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

    public static void generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);

            KeyPair keyPair = generator.generateKeyPair();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("server.pub"));
            out.writeObject(keyPair.getPublic());
            out.close();

            out = new ObjectOutputStream(new FileOutputStream("server.prv"));
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
