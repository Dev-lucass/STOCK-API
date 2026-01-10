package com.example.estoque_api.exceptions;

public class ToolInUseException extends RuntimeException {
    public ToolInUseException(String message) {
        super(message);
    }
}
