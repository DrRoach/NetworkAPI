package main.java.com.github.networkapi.exceptions;

public class InvalidServerSignatureException extends RuntimeException {
    public InvalidServerSignatureException(String message) {
        super(message);
    }
}
