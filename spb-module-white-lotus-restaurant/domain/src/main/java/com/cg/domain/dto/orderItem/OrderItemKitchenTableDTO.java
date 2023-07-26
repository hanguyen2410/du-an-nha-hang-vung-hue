package com.cg.domain.dto.orderItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemKitchenTableDTO {

    private Long orderItemId;
    private String tableName;
    private Long productId;
    private String productTitle;
    private String note;
    private int quantity;
    private String unitTitle;

    private Boolean cooking;

//    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedAt;

}
