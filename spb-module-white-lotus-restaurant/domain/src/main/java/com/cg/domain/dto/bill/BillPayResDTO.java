package com.cg.domain.dto.bill;



import com.cg.domain.dto.orderItem.OrderItemBillResDTO;
import com.cg.domain.dto.orderItem.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillPayResDTO {

    private Long orderId;

    private Long tableId;

    private BigDecimal totalAmount;

    List<OrderItemBillResDTO> items;
}
