package com.cg.domain.dto.product;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.product.avatar.ProductAvatarDTO;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.product.ProductAvatar;
import com.cg.domain.entity.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResDTO {
    private Long productId;
    private String productTitle;
    private BigDecimal price;
    private String unitTitle;
    private Long unitId;
    private Boolean cooking;
    private CategoryDTO category;
    private String description;
    private ProductAvatarDTO productAvatar;
    private Boolean outStock;


    public ProductResDTO(Long productId, String productTitle, BigDecimal price, Unit unit, Boolean cooking, Category category, String description, ProductAvatar productAvatar, Boolean outStock) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.price = price;
        this.unitTitle = unit.getTitle();
        this.unitId = unit.getId();
        this.cooking = cooking;
        this.category = category.toCategoryDTO();
        this.description = description;
        this.productAvatar = productAvatar.toProductAvatarDTO();
        this.outStock = outStock;
    }

}
