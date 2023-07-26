package com.cg.domain.dto.orderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemKitchenGroupDTO {

    private Long productId;
    private String productTitle;
    private String note;
    private Long quantity;

    private String unitTitle;


}
