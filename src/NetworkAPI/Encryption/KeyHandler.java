package NetworkAPI.Encryption;

import NetworkAPI.Exceptions.ExceptionCodes;
import NetworkAPI.Exceptions.FileNotFoundException;
import NetworkAPI.FileHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;

public class KeyHandler {
    public static Key readPublic(String keyFile) {
        Key key = null;

        try {
            FileHandler file = new FileHandler(keyFile + ".pub");

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
}
