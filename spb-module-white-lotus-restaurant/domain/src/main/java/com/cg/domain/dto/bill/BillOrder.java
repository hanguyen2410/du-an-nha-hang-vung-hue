package com.cg.domain.dto.bill;

import com.cg.domain.entity.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderItem_id", referencedColumnName = "id", nullable = false)
    private OrderItem orderItem;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;

    @Column(precision = 12, scale = 0, nullable = false)
    private int quantity;

    @Column(name = "total_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal totalAmount;

}
