package org.wn.rockets.exception;

import lombok.Getter;

/**
 * Exception thrown when trying to add object that is already present in store.
 */
@Getter
public class AlreadyPresentInStoreException extends RuntimeException {
    private final String message;

    public AlreadyPresentInStoreException(String message) {
        super(message);
        this.message = message;
    }
}
