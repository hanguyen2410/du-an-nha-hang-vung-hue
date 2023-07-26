package com.cg.domain.dto.orderItem;

import com.cg.domain.dto.order.OrderResDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.table.TableDTO;
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
public class OrderItemDTO {

    private Long id;
    private BigDecimal price;
    private int quantity;
    private EOrderItemStatus status;
    private BigDecimal amount;
    private String note;
    private TableDTO table;
    private ProductDTO product;
    private OrderResDTO order;


    public OrderItemDTO(Long id, BigDecimal price, int quantity, EOrderItemStatus status, BigDecimal amount, String note, AppTable table, Product product, Order order) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.amount = amount;
        this.note = note;
        this.table = table.toTableDTO();
        this.product = product.toProductDTO();
        this.order = order.toOrderDTO();
    }

    public OrderItem toOrderItem(){
        return new OrderItem()
                .setId(id)
                .setPrice(price)
                .setQuantity(quantity)
                .setUnit(product.getUnit().getTitle())
                .setStatus(status)
                .setAmount(amount)
                .setNote(note)
                .setTable(table.toTable())
                .setProduct(product.toProduct())
                .setOrder(order.toOrder());
    }

}
