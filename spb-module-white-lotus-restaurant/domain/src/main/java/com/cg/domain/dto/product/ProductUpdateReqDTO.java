package com.cg.domain.dto.product;

import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.product.ProductAvatar;
import com.cg.domain.entity.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductUpdateReqDTO implements Validator {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên sản phẩm.")
    private String title;

    @Pattern(regexp = "^\\d+$", message = "Giá sản phẩm phải là số.")
    @NotEmpty(message = "Giá sản phẩm không được để trống.")
    private String price;

    private Long unitId;

    private Long categoryId;

    private String description;

    private String fileType;
    private Boolean cooking;

    public Product toProduct(ProductAvatar productAvatar, Category category, Unit unit) {
        return new Product()
                .setId(id)
                .setTitle(title)
                .setPrice(new BigDecimal(Long.parseLong(price)))
                .setCategory(category)
                .setUnit(unit)
                .setDescription(description)
                .setProductAvatar(productAvatar)
                .setCooking(cooking);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCreateDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCreateDTO productCreateDTO = (ProductCreateDTO) target;
        String price = productCreateDTO.getPrice();

        if (price != null && price.length() > 0) {
            if (price.length() > 9){
                errors.rejectValue("price", "price.max","Giá sản phẩm tối đa là 999.999.999 VNĐ");
                return;
            }

            if (price.length() < 6){
                errors.rejectValue("price", "price.min","Giá sản phẩm thấp nhất là 100.000 VNĐ");
                return;
            }

            if (!price.matches("(^$|[0-9]*$)")){
                errors.rejectValue("price",  "price.number","Giá sản phẩm phải là số.");
                return;
            }

        } else {
            errors.rejectValue("price",  "price.null", "Vui lòng nhập giá sản phẩm.");
        }

    }
}
