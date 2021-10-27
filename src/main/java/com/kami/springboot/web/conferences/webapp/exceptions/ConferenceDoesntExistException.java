package com.kami.springboot.web.conferences.webapp.exceptions;

public final class ConferenceDoesntExistException extends RuntimeException {
    public ConferenceDoesntExistException(final String message) {
        super(message);
    }
}
