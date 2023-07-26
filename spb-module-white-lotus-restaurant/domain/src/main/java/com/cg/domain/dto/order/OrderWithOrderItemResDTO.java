package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemResDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.table.AppTable;
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
public class OrderWithOrderItemResDTO {
    private Long orderId;
    private BigDecimal totalAmount;
    private TableDTO table;
    private List<OrderItemResDTO> orderItems;
}
