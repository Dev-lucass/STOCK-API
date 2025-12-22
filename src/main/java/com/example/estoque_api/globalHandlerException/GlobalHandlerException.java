package com.example.estoque_api.globalHandlerException;

import com.example.estoque_api.dto.response.error.*;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ErrorReturnToInventoryResponseException;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDate;
import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorInvalidArguments invalidArguments(MethodArgumentNotValidException ex) {

        List<ResponseErrorInvalidFields> responseErrorInvalidFields = ex.getFieldErrors()
                .stream()
                .map(field -> new ResponseErrorInvalidFields(
                        field.getDefaultMessage(),
                        field.getField()
                ))
                .toList();

        String message = responseErrorInvalidFields.isEmpty()
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
}
