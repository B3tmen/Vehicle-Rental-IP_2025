package org.unibl.etf.carrentalbackend.exception;

public class EmailConflictException extends RuntimeException {
    public EmailConflictException() { super("An email already exists for this entity."); }

    public EmailConflictException(String message) { super(message); }
}
