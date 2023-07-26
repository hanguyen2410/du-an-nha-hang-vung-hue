package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderWithOrderItemReqDTO {

    private Long tableId;
    private String status;
    List<OrderItemReqDTO> items;

}
