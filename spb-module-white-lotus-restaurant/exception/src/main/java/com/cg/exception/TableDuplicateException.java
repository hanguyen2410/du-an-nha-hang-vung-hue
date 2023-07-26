package com.cg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TableDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TableDuplicateException(String message) {
        super(message);
    }
}
