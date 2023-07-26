package com.cg.domain.dto.orderItem;

import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.enums.EOrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemReqDTO {
    private Long id;
    private int quantity;
    private String note;
    private Long productId;

    public OrderItemReqDTO(Long id, int quantity, String note, Product product){
        this.id = id;
        this.quantity = quantity;
        this.note = note;
        this.productId = product.getId();
    }


    public OrderItem toOrderItem(Product product, BigDecimal amount, EOrderItemStatus eOrderItemStatus, Order order, AppTable appTable) {
        return new OrderItem()
                .setId(id)
                .setProductTitle(product.getTitle())
                .setUnit(product.getUnit().getTitle())
                .setPrice(product.getPrice())
                .setQuantity(quantity)
                .setAmount(amount)
                .setStatus(eOrderItemStatus)
                .setOrder(order)
                .setTable(appTable)
                .setNote(note)
                .setProduct(product)
                ;
    }
}
