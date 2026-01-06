package com.example.estoque_api.exceptions;

public class userMustStillReturnBeforeBeingDeactivated extends RuntimeException {
    public userMustStillReturnBeforeBeingDeactivated(String message) {
        super(message);
    }
}
