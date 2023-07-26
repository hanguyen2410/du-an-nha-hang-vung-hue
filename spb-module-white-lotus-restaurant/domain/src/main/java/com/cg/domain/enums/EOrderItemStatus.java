package com.cg.domain.enums;

import com.cg.exception.DataInputException;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EOrderItemStatus {
    NEW("NEW"),
    UPDATE("UPDATE"),
    COOKING("COOKING"),
    WAITER("WAITER"),
    DELIVERY("DELIVERY"),
    STOCK_OUT("STOCK_OUT"),
    DONE("DONE");

    private final String value;

    EOrderItemStatus(String value) {
        this.value = value;
    }

    private static final Map<String, EOrderItemStatus> NAME_MAP = Stream.of(values())
            .collect(Collectors.toMap(EOrderItemStatus::toString, Function.identity()));

    public static EOrderItemStatus fromString(final String name) {
        EOrderItemStatus orderItemStatus = NAME_MAP.get(name);
        if (null == orderItemStatus) {
            throw new DataInputException(String.format("'%s' has no corresponding value. Accepted values: %s", name, Arrays.asList(values())));
        }
        return orderItemStatus;
    }

    public String getValue() {
        return this.value;
    }

}
