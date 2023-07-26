package com.cg.domain.dto.order;

import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EOrderStatus;
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
public class OrderResDTO {

    private Long id;
    private BigDecimal totalAmount;
    private EOrderStatus status;
    private TableDTO table;
    private Date createdAt;
    private String createdBy;


    public OrderResDTO(Long id, BigDecimal totalAmount, AppTable table, Date createdAt, String createdBy) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.table = table.toTableDTO();
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public OrderResDTO(Long id, BigDecimal totalAmount, AppTable table) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.table = table.toTableDTO();
    }

    public Order toOrder(){
        return new Order()
                .setId(id)
                .setTotalAmount(totalAmount)
                .setTable(table.toTable())
                ;
    }
}

