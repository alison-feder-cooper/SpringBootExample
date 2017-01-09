package com.rewards.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User already exists")
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -3985539392421727736L;

    public UserAlreadyExistsException(long userId, String email) {
        super("User with email " + email + " already exists (User[id= " + userId + "]");
    }
}
