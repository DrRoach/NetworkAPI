package com.gitlab.networkapi.exceptions;

public class ClientConnectionFailedException extends RuntimeException {
    public ClientConnectionFailedException(String message) {
        super(message);
    }
}
