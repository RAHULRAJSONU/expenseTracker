package com.silvertech.expenseTracker.controller.advice;


import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorCodeValidationException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.exception.NotFoundException;
import com.silvertech.expenseTracker.exception.SomeThingWentWrongException;
import com.silvertech.expenseTracker.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ControllerAdvice
public class ExpenseTrackerControllerAdvice {

    @Order(Ordered.LOWEST_PRECEDENCE)

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> genericExceptionHandler(Exception e) {
        log.error("Exception Occurred: {} {}", e.getMessage(), e);
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ErrorCodeException.class)
    public ResponseEntity<?> errorCodeExceptionHandler(ErrorCodeException e) {
        log.error("ErrorCodeException Occurred: {} {}", e.getMessage(), e);
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()),
                e.getHttpStatus());
    }

    @ExceptionHandler(value = ErrorCodeValidationException.class)
    public ResponseEntity<?> ErrorCodeValidationExceptionHandler(ErrorCodeValidationException e) {
        log.error("ErrorCodeValidationException Occurred: {} {}", e.getMessage(), e);
        StringBuilder strBuilder = new StringBuilder();
        for (ErrorMessage msgList : e.getErrorMessages()) {
            strBuilder.append(msgList.getMessage() + ";\n");
        }
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(strBuilder.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = SomeThingWentWrongException.class)
    public ResponseEntity<?> SomeThingWentWrongExceptionHandler(SomeThingWentWrongException e) {
        log.error("SomeThingWentWrongException Occurred: {} {}", e.getMessage(), e);
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> notFoundExceptionHandler(NotFoundException e) {
        log.error("NotFoundException Occurred: {} {}", e.getMessage(), e);
        return new ResponseEntity<Object>(
                e.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException Occurred: {} {} ",
                e.getMessage(), e);
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> errorExceptionHandler(ConstraintViolationException e) {
        log.error("ConstraintViolationException Occurred: {} {} {}", e
                .getMessage(), e.getConstraintViolations(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Set<String> messageSet = new HashSet<String>();
        for (ConstraintViolation<?> violation : violations) {
            messageSet.add(violation.getMessage());
        }

        StringBuilder strBuilder = new StringBuilder();
        for (String messageStr : messageSet) {
            strBuilder.append(messageStr + " ");
        }

        return new ResponseEntity<ErrorMessage>(new ErrorMessage(strBuilder.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> validationExceptionHandler(ValidationException e) {
        log.error("ValidationException Occurred: {} {}", e.getMessage(), e);
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException Occurred: {} {} {} ",
                e.getMessage(), e.getParameterName(), e);
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
    public ResponseEntity<?> unsatisfiedServletRequestParameterException
            (UnsatisfiedServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException Occurred: {} {} {} ",
                e.getMessage(), e);
        return new ResponseEntity<>(new ErrorMessage("Invalid Request " +
                "Parameters"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> methodArgumentTypeMisMatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException Occurred: {} {} ",
                e.getMessage(), e);
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
