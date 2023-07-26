package com.cg.domain.dto.product;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.product.avatar.ProductAvatarDTO;
import com.cg.domain.dto.unit.UnitDTO;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.product.Product;
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
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private UnitDTO unit;
    private CategoryDTO category;
    private ProductAvatarDTO productAvatar;

    public ProductDTO(Long id, String title, String description, Unit unit, Category category, ProductAvatar productAvatar) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.unit = unit.toUnitDTO();
        this.category = category.toCategoryDTO();
        this.productAvatar = productAvatar.toProductAvatarDTO();
    }

    public Product toProduct() {
        return new Product()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setCategory(category.toCategory())
                .setProductAvatar(productAvatar.toProductAvatar());
    }

}
