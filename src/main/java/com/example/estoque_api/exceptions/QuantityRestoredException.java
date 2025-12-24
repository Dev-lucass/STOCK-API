package com.example.estoque_api.exceptions;

public class QuantityRestoredException extends RuntimeException {
    public QuantityRestoredException(String message) {
        super(message);
    }
}
