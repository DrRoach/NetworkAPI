package main.java.com.gitlab.networkapi.Exceptions;

public class PortOutOfRangeException extends RuntimeException {
    public PortOutOfRangeException(String message) {
        super(message);
    }
}
