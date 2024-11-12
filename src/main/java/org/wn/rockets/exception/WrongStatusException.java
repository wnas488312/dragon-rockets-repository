package org.wn.rockets.exception;

import lombok.Getter;

/**
 * Exception thrown when current state of data does not meet precondition.
 */
@Getter
public class WrongStatusException extends RuntimeException {
    private final String message;

    public WrongStatusException(String message) {
        super(message);
        this.message = message;
    }
}
