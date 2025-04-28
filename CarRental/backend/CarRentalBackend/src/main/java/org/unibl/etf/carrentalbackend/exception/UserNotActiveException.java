package org.unibl.etf.carrentalbackend.exception;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException() { super("User is not active"); }

    public UserNotActiveException(String message) { super(message); }
}
