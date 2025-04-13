package com.example.basket.adapters.in.rest;

import com.example.basket.adapters.in.rest.dto.Error;
import com.example.basket.common.exceptions.EntityExistsException;
import com.example.basket.common.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handleNotFound(RuntimeException exception) {
        log.error("Not found", exception);

        var error = new Error(
                HttpStatus.NOT_FOUND.value(),
                Objects.requireNonNullElse(exception.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase())
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Error> handleConflict(RuntimeException exception) {
        log.error("Conflict", exception);

        var error = new Error(
                HttpStatus.CONFLICT.value(),
                Objects.requireNonNullElse(exception.getMessage(), HttpStatus.CONFLICT.getReasonPhrase())
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<Error> handleUnprocessableEntity(RuntimeException exception) {
        log.error("Unprocessable entity", exception);

        var error = new Error(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                Objects.requireNonNullElse(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
        );

        return ResponseEntity.unprocessableEntity().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleInternalServerError(Throwable exception) {
        log.error("Internal server error", exception);

        var error = new Error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        );

        return ResponseEntity.internalServerError().body(error);
    }
}
