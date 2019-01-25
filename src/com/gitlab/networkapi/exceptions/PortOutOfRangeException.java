package com.gitlab.networkapi.exceptions;

public class PortOutOfRangeException extends RuntimeException {
    public PortOutOfRangeException(String message) {
        super(message);
    }
}
