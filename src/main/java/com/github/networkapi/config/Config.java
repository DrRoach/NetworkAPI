package main.java.com.github.networkapi.config;

/**
 * This class is used to store any configuration settings. Please be careful to
 *  setup your system correctly, especially when using encryption, being sure
 *  to replace default values.
 */
public class Config {
    /**
     * This string is used to verify that your clients are connected to your
     *  server and not another server pretending to be yours.
     */
    public static String encryptionSignature = "REPLACE THIS VALUE";
}
