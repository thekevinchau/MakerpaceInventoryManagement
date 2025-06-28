package com.Makerspace.MakerspaceInventoryApp.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GlobalBadRequestException extends RuntimeException {
    public GlobalBadRequestException(String message) {
        super(message);
    }
}
