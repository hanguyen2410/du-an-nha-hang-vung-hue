package com.cg.domain.entity.backup;

import com.cg.domain.entity.BaseEntity;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EOrderItemStatus;
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
@Entity
@Table(name = "bill_backup")
public class BillBackup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unit;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;

    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EOrderItemStatus status;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal amount;

    private String note;
    private Long tableId;
    private Long productId;
    private String productTitle;
    private Long orderId;

    @ManyToOne
    @JoinColumn(name= "table_backup", nullable = false)
    private TableBackup tableBackup;
}
