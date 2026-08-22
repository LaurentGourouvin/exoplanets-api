package com.example.exoplanetes.exceptions;

public class IllegalStatutTransitionException extends RuntimeException {
    public IllegalStatutTransitionException(String message) {
        super(message);
    }
}
