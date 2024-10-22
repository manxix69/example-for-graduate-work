package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PasswordIsNotMatchException extends RuntimeException{
    public PasswordIsNotMatchException(String message) {
        super(message);
    }
}