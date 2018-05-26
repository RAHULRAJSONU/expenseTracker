package com.silvertech.expenseTracker.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SomeThingWentWrongException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
}

