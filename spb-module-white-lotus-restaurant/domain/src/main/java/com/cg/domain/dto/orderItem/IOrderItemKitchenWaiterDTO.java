package com.cg.domain.dto.orderItem;

import java.util.Date;

public interface IOrderItemKitchenWaiterDTO {

    Long getOrderItemId();
    Long getTableId();
    String getTableName();
    Long getProductId();
    String getProductTitle();
    String getNote();
    Long getQuantity();
    String getUnitTitle();
    Date getUpdatedAt();
    Boolean getCooking();
}
