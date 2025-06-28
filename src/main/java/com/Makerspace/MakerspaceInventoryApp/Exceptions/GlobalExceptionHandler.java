package com.Makerspace.MakerspaceInventoryApp.Exceptions;

import com.Makerspace.MakerspaceInventoryApp.Item.ItemNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleUniqueConstraint(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(GlobalNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ItemNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("404 Error: " + ex.getMessage());
    }

    @ExceptionHandler(GlobalBadRequestException.class)
    public ResponseEntity<String> handleBadRequest(GlobalBadRequestException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("400 Error: " + ex.getMessage());
    }

    // Catch-all for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server error: " + ex.getMessage());
    }
}
