package com.HaiDang.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserException extends Exception{
    String message;
    public UserException(String message){
        super(message);
    }
}
