package com.cg.domain.dto.bill;

import com.cg.domain.dto.orderItem.OrderItemResDTO;
import com.cg.domain.entity.bill.Bill;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillCreateDTO {

    private Long discountPercent;

    private Long chargePercent;

    private Long orderId;



    public BillCreateDTO(BigDecimal discountMoney, Long discountPercent, BigDecimal chargeMoney, Long chargePercent, Order order) {
        this.discountPercent = discountPercent;
        this.chargePercent = chargePercent;
        this.orderId = order.getId();
    }

    public Bill toBill(AppTable table, Order order) {
        return new Bill()
                .setDiscountPercent(discountPercent)
                .setChargePercent(chargePercent)
                .setTable(table)
                .setOrder(order)
                ;
    }
}
