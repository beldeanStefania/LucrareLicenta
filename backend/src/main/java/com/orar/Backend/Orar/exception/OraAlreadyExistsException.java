package com.orar.Backend.Orar.exception;

public class OraAlreadyExistsException extends Exception {
    public OraAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
