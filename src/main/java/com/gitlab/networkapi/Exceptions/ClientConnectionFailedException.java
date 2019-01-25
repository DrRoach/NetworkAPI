package main.java.com.gitlab.networkapi.Exceptions;

public class ClientConnectionFailedException extends RuntimeException {
    public ClientConnectionFailedException(String message) {
        super(message);
    }
}
