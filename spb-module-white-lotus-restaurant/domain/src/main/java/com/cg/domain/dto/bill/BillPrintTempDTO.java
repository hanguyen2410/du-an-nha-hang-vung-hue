package com.cg.domain.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillPrintTempDTO {

    private Long orderId;
    private Long tableId;
    private String tableName;
    private Date createdAt;
    private Date updatedAt;
    private String staffName;
    private BigDecimal discountMoney;
    private Long discountPercent;
    private BigDecimal chargeMoney;
    private Long chargePercent;
    private BigDecimal totalAmount;

    List<BillPrintItemDTO> items;
}
