package com.cg.domain.enums;

public enum EOrderStatus {

    PAID("PAID"),
    UNPAID("UNPAID");

    private final String value;

    EOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

