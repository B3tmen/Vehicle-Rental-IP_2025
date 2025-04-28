package org.unibl.etf.carrentalbackend.exception;

public class CustomVehicleIdConflictException extends RuntimeException {
    public CustomVehicleIdConflictException() { super("Custom vehicle ID already exists."); }

    public CustomVehicleIdConflictException(String message) { super(message); }
}
