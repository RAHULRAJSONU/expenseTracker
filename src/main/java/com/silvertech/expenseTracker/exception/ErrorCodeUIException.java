package com.silvertech.expenseTracker.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ErrorCodeUIException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;
    private final List<String> id;
}
