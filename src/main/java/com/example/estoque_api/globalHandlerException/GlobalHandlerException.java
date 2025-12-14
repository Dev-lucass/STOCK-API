package com.example.estoque_api.globalHandlerException;

import com.example.estoque_api.dto.response.ResponseErrorInvalidArguments;
import com.example.estoque_api.dto.response.ResponseErrorInvalidFields;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorInvalidArguments invalidArguments (MethodArgumentNotValidException ex) {

        List<ResponseErrorInvalidFields> responseErrorInvalidFields = ex.getFieldErrors().stream().map(fields -> new ResponseErrorInvalidFields(
                fields.getDefaultMessage(),
                fields.getField()
        )).toList();

        return new ResponseErrorInvalidArguments(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                responseErrorInvalidFields
        );
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorInvalidArguments resourceNotFound (ResourceNotFoundException ex) {
        return new ResponseErrorInvalidArguments(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                List.of()
        );
    }

    @ExceptionHandler(DuplicateResouceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorInvalidArguments duplicateResouce (DuplicateResouceException ex) {
        return new ResponseErrorInvalidArguments(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                List.of()
        );
    }



}
