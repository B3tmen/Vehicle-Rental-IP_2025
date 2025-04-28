package org.unibl.etf.clientapp.model.enums;

public enum CardType {
    VISA("Visa"), MASTER_CARD("MasterCard");

    private final String name;
    private CardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
