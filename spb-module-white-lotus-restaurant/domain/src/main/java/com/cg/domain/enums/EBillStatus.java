package com.cg.domain.enums;

public enum EBillStatus {
    ORDERING("ORDERING"),
    TEMPORARY("TEMPORARY"),
    CANCEL("CANCEL"),
    PAID("PAID");

    private final String value;

    EBillStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
