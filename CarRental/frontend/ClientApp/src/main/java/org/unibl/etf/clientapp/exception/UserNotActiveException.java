package org.unibl.etf.clientapp.exception;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException() { super("User account is not active"); }

    public UserNotActiveException(String message) { super(message); }
}
