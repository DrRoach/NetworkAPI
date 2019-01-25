package com.gitlab.networkapi.exceptions;

public class InvalidServerSignatureException extends RuntimeException {
    public InvalidServerSignatureException(String message) {
        super(message);
    }
}
