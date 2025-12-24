package com.example.estoque_api.globalHandlerException;

import com.example.estoque_api.dto.response.error.*;
import com.example.estoque_api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorInvalidArguments invalidArguments(MethodArgumentNotValidException ex) {

        var responseErrorInvalidFields = ex.getFieldErrors()
                .stream()
                .map(field -> new ResponseErrorInvalidFields(
                        field.getDefaultMessage(),
                        field.getField()
                ))
                .toList();

        var message = responseErrorInvalidFields.isEmpty()
                ? "Invalid request"
                : responseErrorInvalidFields.getFirst().message();

        return new ResponseErrorInvalidArguments(
                HttpStatus.BAD_REQUEST.value(),
                LocalDate.now(),
                responseErrorInvalidFields
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorResouceNotFound resourceNotFound(ResourceNotFoundException ex) {
        return new ResponseErrorResouceNotFound(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDate.now()
        );
    }

    @ExceptionHandler(DuplicateResouceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorConflictValue duplicateResouce(DuplicateResouceException ex) {
        return new ResponseErrorConflictValue(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDate.now()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorInvalidQuantity handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseErrorInvalidQuantity(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDate.now()
        );
    }

    @ExceptionHandler(InvalidQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorInvalidQuantity invalildQuantity(InvalidQuantityException ex) {
        return new ResponseErrorInvalidQuantity(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDate.now()
        );
    }

    @ExceptionHandler(ErrorReturnToInventoryResponseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorReturnToInventory returnToInventory(ErrorReturnToInventoryResponseException ex) {
        return new ResponseErrorReturnToInventory(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDate.now()
        );
    }

    @ExceptionHandler(QuantityRestoredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorReturnToInventory quantityRestored(QuantityRestoredException ex) {
        return new ResponseErrorReturnToInventory(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDate.now()
        );
    }

    @ExceptionHandler(QuantitySoldOutException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseErrorReturnToInventory quantitySoldOut(QuantitySoldOutException ex) {
        return new ResponseErrorReturnToInventory(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                LocalDate.now()
        );
    }
}
