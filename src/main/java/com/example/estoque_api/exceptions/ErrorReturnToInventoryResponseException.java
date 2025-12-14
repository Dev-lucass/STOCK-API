package com.example.estoque_api.exceptions;

public class ErrorReturnToInventoryResponseException extends RuntimeException {
    public ErrorReturnToInventoryResponseException(String message) {
        super(message);
    }
}
