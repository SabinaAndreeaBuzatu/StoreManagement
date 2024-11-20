package com.store.storemanagement.exception;

public class InvalidOrderStatusTransitionException extends RuntimeException {
    public InvalidOrderStatusTransitionException(String initialStatus, String updatedStatus) {
        super("Invalid transition from " + initialStatus + " to " + updatedStatus);
    }
}
