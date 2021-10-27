package com.kami.springboot.web.conferences.webapp.exceptions;

public final class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

}
