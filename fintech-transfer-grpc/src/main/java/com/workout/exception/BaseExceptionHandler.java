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
    @ExceptionHandler(TransferFailedException.class)
    public String accountNotFoundException(TransferFailedException exception) {
        return exception.getMessage();
    }


}