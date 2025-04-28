package org.unibl.etf.carrentalbackend.exception;

public class UsernameConflictException extends RuntimeException {
    public UsernameConflictException() { super("A User with the same username already exists."); }

    public UsernameConflictException(String message) { super(message); }
}
