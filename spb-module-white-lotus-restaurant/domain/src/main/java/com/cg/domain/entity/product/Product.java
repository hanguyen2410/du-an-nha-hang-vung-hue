package com.cg.domain.entity.product;

import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.product.ProductResDTO;
import com.cg.domain.entity.BaseEntity;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.unit.Unit;
import com.cg.domain.enums.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(precision = 10, scale = 0, nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EProductStatus status;

    @OneToOne
    @JoinColumn(name = "product_avatar_id", referencedColumnName = "id")
    private ProductAvatar productAvatar;

    @Column(columnDefinition = "boolean default true")
    private Boolean cooking;

    @Column(columnDefinition = "boolean default false")
    private Boolean outStock;

    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setTitle(title)
                .setPrice(price)
                .setUnit(unit.toUnitDTO())
                .setCategory(category.toCategoryDTO())
                .setUnit(unit.toUnitDTO())
                .setDescription(description)
                .setProductAvatar(productAvatar.toProductAvatarDTO())
                ;
    }

    public ProductResDTO toProductResDTO() {
        return new ProductResDTO()
                .setProductId(id)
                .setProductTitle(title)
                .setPrice(price)
                .setUnitTitle(unit.getTitle())
                .setUnitId(unit.getId())
                .setCategory(category.toCategoryDTO())
                .setDescription(description)
                .setProductAvatar(productAvatar.toProductAvatarDTO())
                .setCooking(cooking)
                .setOutStock(outStock)
                ;
    }

}
