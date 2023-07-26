package com.cg.domain.dto.orderItem;

import com.cg.domain.dto.product.avatar.ProductAvatarDTO;
import com.cg.domain.entity.order.Order;
import com.cg.domain.entity.order.OrderItem;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.product.ProductAvatar;
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
public class OrderItemResDTO {

    private Long id;
    private String unitTitle;
    private BigDecimal price;
    private int quantity;
    private EOrderItemStatus status;
    private BigDecimal amount;
    private String note;
    private Long productId;
    private String productTitle;
    private ProductAvatarDTO productAvatar;

    public OrderItemResDTO(Long id, String unitTitle, BigDecimal price, int quantity, EOrderItemStatus status, BigDecimal amount, String note, Long productId, String productTitle, ProductAvatar productAvatar) {
        this.id = id;
        this.unitTitle = unitTitle;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.amount = amount;
        this.note = note;
        this.productId = productId;
        this.productTitle = productTitle;
        this.productAvatar = productAvatar.toProductAvatarDTO();
    }

    public OrderItem toOrderItem(Product product, Order order) {
        return new OrderItem()
                .setId(id)
                .setUnit(unitTitle)
                .setPrice(price)
                .setQuantity(quantity)
                .setStatus(status)
                .setAmount(amount)
                .setNote(note)
                .setProduct(product)
                .setOrder(order)
                ;
    }
}
