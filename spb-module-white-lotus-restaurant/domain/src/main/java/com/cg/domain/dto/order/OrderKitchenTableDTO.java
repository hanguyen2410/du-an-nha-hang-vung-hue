package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.IOrderItemKitchenTableDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderKitchenTableDTO {

    private Long tableId;

    private String tableName;

    private Date updatedAt;

    private int countProduct;

    private Long orderId;

//    private List<OrderItemKitchenTableDTO> orderItems;
    private List<IOrderItemKitchenTableDTO> orderItems;
}
