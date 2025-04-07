package org.acelera.blogmaker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ThemeAlreadyExistException extends RuntimeException {
    public ThemeAlreadyExistException(String message) {
        super(message);
    }
}
