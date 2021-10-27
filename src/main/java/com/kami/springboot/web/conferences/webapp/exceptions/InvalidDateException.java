package com.kami.springboot.web.conferences.webapp.exceptions;

public final class InvalidDateException extends RuntimeException {

    public InvalidDateException(final String message) {
        super(message);
    }

}
