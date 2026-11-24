package org.exam.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExist extends RuntimeException {
    public AlreadyExist(String message) {
        super(message);
    }
}
