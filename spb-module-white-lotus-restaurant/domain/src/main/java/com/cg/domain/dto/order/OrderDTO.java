package com.cg.domain.dto.order;

import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
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
public class OrderDTO {

    private Long id;

    private BigDecimal totalAmount;

    private String orderStatus;

    private TableDTO table;

    private Date updatedAt;

    public OrderDTO(Long id, BigDecimal totalAmount, EBillStatus orderStatus, AppTable table, Date updatedAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus.getValue();
        this.table = table.toTableDTO();
        this.updatedAt = updatedAt;
    }
}
