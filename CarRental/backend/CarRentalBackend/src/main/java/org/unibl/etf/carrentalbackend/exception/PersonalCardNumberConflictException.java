package org.unibl.etf.carrentalbackend.exception;

public class PersonalCardNumberConflictException extends RuntimeException {
    public PersonalCardNumberConflictException() { super("A client with this card ID already exists!"); }

    public PersonalCardNumberConflictException(String message) { super(message); }
}
