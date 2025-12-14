package com.example.estoque_api.exceptions;

public class DuplicateResouceException extends RuntimeException {
    public DuplicateResouceException(String message) {
        super(message);
    }
}
