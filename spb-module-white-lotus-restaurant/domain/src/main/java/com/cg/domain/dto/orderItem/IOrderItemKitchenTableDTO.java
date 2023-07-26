package com.cg.domain.dto.orderItem;

import java.util.Date;

public interface IOrderItemKitchenTableDTO {
    Long getOrderItemId();
    String getTableName();
    Long getProductId();
    String getProductTitle();
    String getNote();
    int getQuantity();
    String getUnitTitle();
    Date getUpdatedAt();
}
