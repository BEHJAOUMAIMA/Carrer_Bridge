package com.example.carrer_bridge.handler.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMessage {
    public Integer status;
    public String message;

    public Object data;

    public ResponseMessage() {
    }

    public ResponseMessage(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }
    // ok
    public static ResponseEntity<ResponseMessage> ok(String message, Object data) {
        return new ResponseEntity<ResponseMessage>(new ResponseMessage(HttpStatus.OK.value(), message, data), HttpStatus.OK);
    }

    // created
    public static ResponseEntity<ResponseMessage> created(String message, Object data) {
        return new ResponseEntity<ResponseMessage>(new ResponseMessage(HttpStatus.CREATED.value(), message, data), HttpStatus.CREATED);
    }

    // bad request
    public static ResponseEntity<ResponseMessage> badRequest(String message) {
        return new ResponseEntity<ResponseMessage>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
    }

    // not found
    public static ResponseEntity<ResponseMessage> notFound(String message) {
        return new ResponseEntity<ResponseMessage>(new ResponseMessage(HttpStatus.NOT_FOUND.value(), message), HttpStatus.NOT_FOUND);
    }

}
