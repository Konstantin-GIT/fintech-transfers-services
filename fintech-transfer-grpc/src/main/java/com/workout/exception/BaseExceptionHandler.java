package com.workout.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@ResponseBody
@ControllerAdvice
public class BaseExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(TransactionFailedException.class)
    public String accountNotFoundException(TransactionFailedException exception) {
        return exception.getMessage();
    }


}