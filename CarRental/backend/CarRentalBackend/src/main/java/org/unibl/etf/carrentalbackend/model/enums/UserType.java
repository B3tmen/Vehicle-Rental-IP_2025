package org.unibl.etf.carrentalbackend.model.enums;

public enum UserType {

    Client("Client"), Employee("Employee");

    private final String value;

    UserType(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
