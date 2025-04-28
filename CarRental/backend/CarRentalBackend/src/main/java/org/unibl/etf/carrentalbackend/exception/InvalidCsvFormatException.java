package org.unibl.etf.carrentalbackend.exception;

public class InvalidCsvFormatException extends RuntimeException {
    public InvalidCsvFormatException() {
        super("Invalid csv format.");
    }

    public InvalidCsvFormatException(String message) {
        super(message);
    }
}
