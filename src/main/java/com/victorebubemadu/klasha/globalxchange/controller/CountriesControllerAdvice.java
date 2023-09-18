package com.victorebubemadu.klasha.globalxchange.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.victorebubemadu.klasha.globalxchange.exception.AppException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice(basePackageClasses = CountriesController.class)
public class CountriesControllerAdvice {

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Response<?>> handleValidationException(ConstraintViolationException ex) {

        String errMsg = "";

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errMsg = violation.getMessage();
            break;
        }

        var message = "Validation Failed";
        var exception = new AppException("VALIDATION_ERROR", errMsg);
        var response = new Response<>(message, exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<?>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var message = "Validation Failed";
        var errorDetails = "Please check the request data with the name: " + ex.getPropertyName();
        var exception = new AppException("VALIDATION_ERROR", "INCOMPATIBLE_TYPE_ERROR", errorDetails);
        var response = new Response<>(message, exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response<?>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        var message = "Validation Failed";
        var errorDetails = "Please check the request data with the name: " + ex.getParameterName();
        var exception = new AppException("VALIDATION_ERROR", "MISSING_REQUIRED_DATA", errorDetails);
        var response = new Response<>(message, exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({ AppException.class })
    public ResponseEntity<Response<?>> handleInSensitiveAppException(AppException ex) {

        var message = "Operation Failed";
        var response = new Response<>(message, ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Response<?>> handleSensitiveAppException(Exception ex) {

        System.out.println("UNCAUGHT: " + ex);

        var message = "Error Occurred";
        var exception = new AppException("APP_ERROR", "UNEXPECTED_ISSUE");
        var response = new Response<>(message,
                exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<String>
    // handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    // String errorMessage = "Validation failed for the limit parameter.";
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    // }

}
