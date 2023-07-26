package com.cg.domain.dto.bill;

import com.cg.domain.dto.order.OrderResDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.table.TableDTO;
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
public class BillPrintItemDTO {

    private String productTitle;
    private Long quantity;
    private String unitTitle;
    private BigDecimal price;
    private BigDecimal amount;

}
