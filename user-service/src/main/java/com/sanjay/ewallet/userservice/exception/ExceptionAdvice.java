package com.sanjay.ewallet.userservice.exception;

import com.sanjay.ewallet.userservice.util.ResponseGenerator;
import com.sanjay.ewallet.userservice.util.LoggerWrapper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerWrapper.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> genericRuntimeExceptionHandler(RuntimeException ex, WebRequest request) {
        LOG.error("Runtime Exception: " + ex);
        return new ResponseEntity<>(ResponseGenerator.genericErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { FinalException.class })
    protected ResponseEntity<Object> finalExceptionHandler(FinalException ex, WebRequest request) {
        LOG.error("Final Exception: " + ex);
        return new ResponseEntity<>(ResponseGenerator.genericErrorResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> genericExceptionHandler(Exception ex, WebRequest request) {
        LOG.error("Generic Exception: " + ex);
        return new ResponseEntity<>(ResponseGenerator.genericErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
