package com.cg.domain.enums;

 public enum EProductStatus {
    STOCKING("STOCKING"),
    SOLD_OUT("SOLD_OUT");

    private final String value;

    EProductStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
