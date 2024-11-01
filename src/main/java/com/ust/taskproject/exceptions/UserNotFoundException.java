package com.ust.taskproject.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    private String message;

    public UserNotFoundException(String message){
        super(message);
        this.message=message;
    }
}
