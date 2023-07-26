package com.cg.domain.entity.bill;

import com.cg.domain.dto.bill.*;
import com.cg.domain.dto.orderItem.OrderItemBillResDTO;
import com.cg.domain.entity.BaseEntity;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bills")
public class Bill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_price", precision = 12, scale = 0, nullable = false)
    private BigDecimal orderPrice;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal discountMoney;

    private Long discountPercent;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal chargeMoney;

    private Long chargePercent;

    @Column(name = "total_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id", nullable = false)
    private AppTable table;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id", nullable = false)
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", unique = true, nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EBillStatus status;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal cashPay;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal transferPay;


    public Bill initBillPrePay(Order currentOrder, Staff currentStaff) {
        return new Bill()
                .setId(id)
                .setOrder(currentOrder)
                .setStaff(currentStaff)
                .setTable(currentOrder.getTable())
                .setOrderPrice(currentOrder.getTotalAmount())
                .setChargePercent(0L)
                .setChargeMoney(BigDecimal.ZERO)
                .setDiscountPercent(0L)
                .setDiscountMoney(BigDecimal.ZERO)
                .setTotalAmount(currentOrder.getTotalAmount())
                .setStatus(EBillStatus.ORDERING)
                ;

    }

    public BillResDTO toBillResDTO() {
        return new BillResDTO()
                .setOrderPrice(orderPrice)
                .setDiscountMoney(discountMoney)
                .setDiscountPercent(discountPercent)
                .setChargeMoney(chargeMoney)
                .setChargePercent(chargePercent)
                .setTotalAmount(totalAmount)
                .setTableId(table.getId())
                .setOrderId(order.getId())
                .setStatus(status)
                ;
    }

    public BillGetAllResDTO toBillGetAllResDTO( ) {
        return new BillGetAllResDTO()
                .setId(id)
                .setOrderPrice(orderPrice)
                .setDiscountMoney(discountMoney)
                .setDiscountPercent(discountPercent)
                .setChargeMoney(chargeMoney)
                .setChargePercent(chargePercent)
                .setTotalAmount(totalAmount)
                .setTableId(table.getId())
                .setTableName(table.getName())
                .setOrderId(order.getId())
                .setStaffId(staff.getId())
                .setStaffName(staff.getFullName())
                .setCreatedAt(getCreatedAt())
                .setStatus(status)
                .setCashPay(cashPay)
                .setTransferPay(transferPay)
                ;
    }
//
    public BillPayResDTO toBillPayResDTO(List<OrderItemBillResDTO> orderItemDTOList) {
        return new BillPayResDTO()
                .setTableId(table.getId())
                .setTotalAmount(totalAmount)
                .setOrderId(order.getId())
                .setItems(orderItemDTOList)
                ;
    }

    public BillPrintTempDTO toBillPrintTempDTO(List<BillPrintItemDTO> itemDTOS, Date createdAt) {
        return new BillPrintTempDTO()
                .setOrderId(order.getId())
                .setTableId(table.getId())
                .setTableName(table.getName())
                .setCreatedAt(order.getCreatedAt())
                .setUpdatedAt(getUpdatedAt())
                .setStaffName(staff.getFullName())
                .setDiscountMoney(discountMoney)
                .setDiscountPercent(discountPercent)
                .setChargeMoney(chargeMoney)
                .setChargePercent(chargePercent)
                .setTotalAmount(totalAmount)
                .setItems(itemDTOS)
                ;
    }

}
