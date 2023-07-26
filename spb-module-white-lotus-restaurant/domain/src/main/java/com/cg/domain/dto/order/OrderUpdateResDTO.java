package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderUpdateResDTO {

    private Long orderId;
    private Long staffId;
    private Long tableId;
    private BigDecimal totalAmount;

    private List<OrderItemResDTO> orderItems;
}
