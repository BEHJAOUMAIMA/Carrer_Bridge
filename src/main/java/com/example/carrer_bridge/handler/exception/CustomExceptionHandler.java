package com.example.carrer_bridge.handler.exception;

import com.example.carrer_bridge.handler.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, List<String>> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(OperationException.class)
    public ResponseEntity<ResponseMessage> handleOperationException(OperationException ex) {

        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return ResponseEntity.badRequest().body(responseMessage);
    }

}
