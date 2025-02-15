package com.orar.Backend.Orar.exception;

public class StudentAlreadyExistsException extends RuntimeException{
    public StudentAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
