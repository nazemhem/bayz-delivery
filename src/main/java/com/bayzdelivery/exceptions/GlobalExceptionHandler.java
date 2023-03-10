package com.bayzdelivery.exceptions;

import java.util.AbstractMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<AbstractMap<String, String>> handle(Exception exception) {
        LOG.error("Request could not be processed: ", exception);
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        res.put("message", "Request could not be processed");
        res.put("details", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
