package com.orar.Backend.Orar.exception;

public class MaterieDoesNotExistException extends Exception {
    public MaterieDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
