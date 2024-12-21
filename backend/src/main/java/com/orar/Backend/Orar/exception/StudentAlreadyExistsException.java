package com.orar.Backend.Orar.exception;

public class StudentAlreadyExistsException extends Exception{
    public StudentAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
