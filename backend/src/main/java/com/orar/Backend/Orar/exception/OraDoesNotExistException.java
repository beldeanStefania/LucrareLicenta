package com.orar.Backend.Orar.exception;

public class OraDoesNotExistException extends Exception {
    public OraDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
