package com.cg.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductFilterReqDTO {

    private Integer currentPageNumber;
    private String keyWord;
    private Integer categoryFilter;
    private String unitFilter;
}
