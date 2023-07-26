package com.cg.domain.entity.order;

import com.cg.domain.dto.orderItem.OrderItemKitchenWaiterDTO;
import com.cg.domain.dto.orderItem.OrderItemResDTO;
import com.cg.domain.entity.BaseEntity;
import com.cg.domain.entity.backup.BillBackup;
import com.cg.domain.entity.backup.TableBackup;
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
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unit;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;

    private int quantity;

    @Column(nullable = false)
    private String productTitle;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EOrderItemStatus status;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal amount;

    private String note;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private AppTable table;

    @ManyToOne
    @JoinColumn(name = "product_id", updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


     public OrderItemResDTO toOrderItemResDTO() {
        return new OrderItemResDTO()
                .setId(id)
                .setUnitTitle(product.getUnit().getTitle())
                .setPrice(product.getPrice())
                .setQuantity(quantity)
                .setAmount(amount)
                .setStatus(status)
                .setNote(note)
                .setProductId(product.getId())
                .setProductTitle(productTitle)
                ;
    }

    public OrderItemKitchenWaiterDTO toOrderItemKitchenWaiterDTO() {
        return new OrderItemKitchenWaiterDTO()
                .setOrderItemId(id)
                .setTableId(table.getId())
                .setTableName(table.getName())
                .setProductId(product.getId())
                .setProductTitle(productTitle)
                .setNote(note)
                .setQuantity((long) quantity)
                .setUnitTitle(unit)
                .setCooking(product.getCooking())
                .setUpdatedAt(getUpdatedAt())
                ;
    }

    public OrderItem initValue(OrderItem newOrderItem) {
        return new OrderItem()
                .setId(id)
                .setUnit(newOrderItem.getUnit())
                .setOrder(newOrderItem.getOrder())
                .setStatus(newOrderItem.getStatus())
                .setTable(newOrderItem.getTable())
                .setPrice(newOrderItem.getPrice())
                .setQuantity(newOrderItem.getQuantity())
                .setAmount(newOrderItem.getAmount())
                .setProduct(newOrderItem.getProduct())
                .setProductTitle(newOrderItem.getProductTitle())
                .setNote(newOrderItem.getNote())
                ;
    }

    public OrderItem backupValue(BillBackup item, Order order, AppTable appTable, Product product) {
        return new OrderItem()
                .setId(id)
                .setUnit(item.getUnit())
                .setOrder(order)
                .setStatus(item.getStatus())
                .setTable(appTable)
                .setPrice(item.getPrice())
                .setQuantity(item.getQuantity())
                .setAmount(item.getAmount())
                .setProduct(product)
                .setProductTitle(item.getProductTitle())
                .setNote(item.getNote())
                ;
    }

    public BillBackup toBillBackup(TableBackup tableBackup){
        return new BillBackup()
                .setId(id)
                .setPrice(price)
                .setQuantity(quantity)
                .setUnit(unit)
                .setStatus(status)
                .setAmount(amount)
                .setNote(note)
                .setTableId(table.getId())
                .setProductId(product.getId())
                .setProductTitle(productTitle)
                .setOrderId(order.getId())
                .setTableBackup(tableBackup)
                ;
    }


}
