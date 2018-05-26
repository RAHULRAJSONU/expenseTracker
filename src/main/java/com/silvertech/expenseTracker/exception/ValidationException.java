package com.silvertech.expenseTracker.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ValidationException extends RuntimeException {

    private final String message;
}
