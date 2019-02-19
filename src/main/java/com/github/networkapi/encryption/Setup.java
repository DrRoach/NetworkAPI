package main.java.com.github.networkapi.encryption;

import main.java.com.github.networkapi.config.Config;

import java.io.File;
import java.security.Key;

public class Setup {
    private byte[] signature;

    public Setup(KeyHandler.Type type)
    {
        String keyDir = getKeyDir(type);

        if (!keysExist(keyDir + "key.pub", keyDir + "key.prv")) {
            KeyHandler.generateKeyPair(type);
        }

        if (type == KeyHandler.Type.Server) {
            Key privateKey = KeyHandler.readPrivate("key", type);

            this.signature = ServerSignature.generateSignature(privateKey, Config.encryptionSignature);
        }
    }

    public byte[] getSignature()
    {
        if (signature != null) {
            return signature;
        }

        return null;
    }
    
    private boolean keysExist(String publicPath, String privatePath)
    {
        return keyExists(publicPath) && keyExists(privatePath);
    }
    
    private boolean keyExists(String path)
    {
        File keyFile = new File(path);

        return keyFile.isFile();
    }
    
    private String getKeyDir(KeyHandler.Type type)
    {
        if (type == KeyHandler.Type.Client) {
            return "./client/";
        } else {
            return "./server/";
        }
    }
}
