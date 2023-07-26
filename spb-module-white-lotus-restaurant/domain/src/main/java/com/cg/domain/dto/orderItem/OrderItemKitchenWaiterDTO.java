package com.cg.domain.dto.orderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemKitchenWaiterDTO {

    private Long orderItemId;
    private Long tableId;
    private String tableName;
    private Long productId;
    private String productTitle;
    private String note;
    private Long quantity;
    private String unitTitle;
    private Boolean cooking;
    private Date updatedAt;
}
