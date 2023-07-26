package com.cg.domain.enums;


public enum ERole {
    ROLE_ADMIN("ADMIN"),
    ROLE_CASHIER("CASHIER"),
    ROLE_KITCHEN("KITCHEN"),
    ROLE_WAITER("WAITER"),
    ROLE_STAFF("STAFF"),
    ROLE_CUSTOMER("CUSTOMER"),
    ROLE_ACCOUNTANT("ACCOUNTANT"),
    ROLE_MANAGER("MANAGER");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
