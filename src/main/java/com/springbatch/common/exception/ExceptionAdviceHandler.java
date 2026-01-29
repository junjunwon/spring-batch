package com.springbatch.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviceHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity handleApiException(CustomException customException) {
        return new ResponseEntity<>(new ErrorDto(
            customException.getExceptionStatus().getStatusCode(),
            customException.getExceptionStatus().getMessage()),
            HttpStatus.valueOf(customException.getExceptionStatus()
                .getStatusCode()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ErrorDto handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn(e.getMessage());
        return new ErrorDto(404, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({SecurityException.class})
    protected ErrorDto SecurityExceptionHandler(SecurityException e) {
        log.warn(e.getMessage());
        return new ErrorDto(403, e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchElementException.class})
    protected ErrorDto handleMethodNotFindException(NoSuchElementException e) {
        log.warn(e.getMessage());
        return new ErrorDto(403, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity handleMethodNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getBindingResult().getFieldError().getDefaultMessage(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity handleEtcException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}


