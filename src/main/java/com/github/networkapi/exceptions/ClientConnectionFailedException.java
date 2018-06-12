package main.java.com.github.networkapi.exceptions;

public class ClientConnectionFailedException extends RuntimeException {
    public ClientConnectionFailedException(String message) {
        super(message);
    }
}
