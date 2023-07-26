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
public class BillGetAllResDTO {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
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
    private BigDecimal cashPay;
    private BigDecimal transferPay;


    public BillGetAllResDTO(Long id, Date createdAt, BigDecimal orderPrice, BigDecimal discountMoney, Long discountPercent, BigDecimal chargeMoney, Long chargePercent, BigDecimal totalAmount, Long tableId, String tableName, Long orderId, Long staffId, String staffName, EBillStatus status) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderPrice = orderPrice;
        this.discountMoney = discountMoney;
        this.discountPercent = discountPercent;
        this.chargeMoney = chargeMoney;
        this.chargePercent = chargePercent;
        this.totalAmount = totalAmount;
        this.tableId = tableId;
        this.tableName = tableName;
        this.orderId = orderId;
        this.staffId = staffId;
        this.staffName = staffName;
        this.status = status;
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
