package org.unibl.etf.carrentalbackend.exception;

import org.unibl.etf.carrentalbackend.util.Constants;

public class InvalidDateTimeFormatException extends RuntimeException {
    public InvalidDateTimeFormatException() { super("Date (time) format is not valid. Adhere to: '" + Constants.DATE_TIME_FORMAT + "'"); }
    public InvalidDateTimeFormatException(String message) { super(message); }
}
