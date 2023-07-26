package com.cg.domain.entity.order;

import com.cg.domain.dto.order.OrderWithOrderItemResDTO;
import com.cg.domain.dto.order.OrderResDTO;
import com.cg.domain.dto.orderItem.OrderItemResDTO;
import com.cg.domain.entity.BaseEntity;
import com.cg.domain.entity.bill.Bill;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EBillStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id", nullable = false)
    private AppTable table;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EBillStatus status;

//    @OneToOne(mappedBy = "order")
//    private Bill bill;


    public OrderResDTO toOrderDTO(){
        return new OrderResDTO()
                .setId(id)
                .setTotalAmount(totalAmount)
                .setTable(table.toTableDTO())
                ;
    }

    public OrderWithOrderItemResDTO toOrderWithOrderItemResDTO(List<OrderItemResDTO> orderItemResDTOS) {
        return new OrderWithOrderItemResDTO()
                .setOrderId(id)
                .setTotalAmount(totalAmount)
                .setTable(table.toTableDTO())
                .setOrderItems(orderItemResDTOS)
                ;
    }

}
