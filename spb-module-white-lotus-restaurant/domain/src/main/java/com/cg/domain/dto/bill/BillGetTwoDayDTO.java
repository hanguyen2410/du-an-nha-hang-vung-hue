package com.cg.domain.dto.bill;

import com.cg.domain.entity.bill.Bill;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillGetTwoDayDTO {
    private Long id;
    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    private Date createdAt;

    private BigDecimal orderPrice;

    private BigDecimal discountMoney;

    private Long discountPercent;

    private BigDecimal chargeMoney;

    private Long chargePercent;

    private BigDecimal totalAmount;

    private Long tableId;
    private String tableName;

    private Long orderId;

    private Long staffId;

    private String staffName;

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
