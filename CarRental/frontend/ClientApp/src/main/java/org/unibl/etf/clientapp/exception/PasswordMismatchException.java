package org.unibl.etf.clientapp.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() { super("Passwords do not match"); }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
