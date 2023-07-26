package com.cg.domain.dto.bill;

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
public class BillResDTO {

    private BigDecimal orderPrice;

    private BigDecimal discountMoney;

    private Long discountPercent;

    private BigDecimal chargeMoney;

    private Long chargePercent;

    private BigDecimal totalAmount;

    private Long tableId;

    private Long orderId;

    private EBillStatus status;

    public Bill toBill(AppTable table, Order order) {
        return new Bill()
                .setDiscountPercent(discountPercent)
                .setChargePercent(chargePercent)
                .setTable(table)
                .setOrder(order)
                ;
    }
}
