package com.cg.domain.dto.orderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemBillHistoryDTO {
    private Long id;
    private String productTitle;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal amount;
}
