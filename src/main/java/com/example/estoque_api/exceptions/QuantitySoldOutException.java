package com.example.estoque_api.exceptions;

public class QuantitySoldOutException extends RuntimeException {
    public QuantitySoldOutException(String message) {
        super(message);
    }
}
