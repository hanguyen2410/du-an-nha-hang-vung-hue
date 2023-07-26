package com.cg.domain.dto.orderItem;

import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.unit.UnitDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.table.AppTable;
import com.cg.domain.entity.unit.Unit;
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
public class OrderItemCurrentDTO {

    private Long id;
    private String unit;
    private BigDecimal price;
    private int quantity;
    private EOrderItemStatus status;
    private BigDecimal amount;
    private String note;
    private ProductDTO product;

    public OrderItemCurrentDTO(Long id, String unit, BigDecimal price, int quantity, EOrderItemStatus status, BigDecimal amount, String note, Product product) {
        this.id = id;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.amount = amount;
        this.note = note;
        this.product = product.toProductDTO();
    }

    public OrderItem toOrderItem(Order order, AppTable appTable) {
        return new OrderItem()
                .setId(id)
                .setUnit(unit)
                .setPrice(price)
                .setQuantity(quantity)
                .setAmount(amount)
                .setStatus(EOrderItemStatus.COOKING)
                .setOrder(order)
                .setTable(appTable)
                .setNote(note)
                .setProduct(product.toProduct())
                ;
    }

}
