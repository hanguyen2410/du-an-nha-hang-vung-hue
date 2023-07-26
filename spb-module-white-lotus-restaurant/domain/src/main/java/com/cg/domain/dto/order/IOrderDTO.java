package com.cg.domain.dto.order;

import java.math.BigDecimal;
import java.util.Date;

public interface IOrderDTO {
    Long getId();
    BigDecimal getTotalAmount();
    String getOrderStatus();
    Long getTableId();
    String getTableName();
    Date getUpdatedAt();
}
