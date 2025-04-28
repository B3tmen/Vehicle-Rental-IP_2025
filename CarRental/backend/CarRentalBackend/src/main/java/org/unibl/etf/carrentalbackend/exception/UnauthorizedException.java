package org.unibl.etf.carrentalbackend.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Unauthorized request.");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
