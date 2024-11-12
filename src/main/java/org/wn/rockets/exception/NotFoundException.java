package org.wn.rockets.exception;

import lombok.Getter;

/**
  *Exception thrown when trying to access object that is not present in store.
 */
@Getter
public class NotFoundException extends RuntimeException {
    private final String message;

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
