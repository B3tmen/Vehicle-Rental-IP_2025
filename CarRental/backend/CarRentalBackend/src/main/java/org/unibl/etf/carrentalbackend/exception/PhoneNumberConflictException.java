package org.unibl.etf.carrentalbackend.exception;

public class PhoneNumberConflictException extends RuntimeException {
    public PhoneNumberConflictException() { super("An identical phone number already exists."); }

    public PhoneNumberConflictException(String message) { super(message); }
}
