package com.cg.service.product;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.product.*;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.unit.Unit;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductService extends IGeneralService<Product, Long> {

    Page<ProductResDTO> findAll(ProductFilterReqDTO productFilterReqDTO, Pageable pageable);

    List<ProductResDTO> findAllProductResDTO();

    List<ProductResDTO> findAllProductResDTOBySearch(String keySearch);

    List<ProductResDTO> findAllProductResDTOByCategoryId(Long categoryId);


    List<ProductResDTO> getTop10SalesProductLastMonth();

    boolean existsByTitle(String title);

    Product createWithAvatar(ProductCreateReqDTO productCreateReqDTO, MultipartFile file, Category category, Unit unit);

    Product createNoAvatar(ProductCreateReqDTO productCreateReqDTO, Category category, Unit unit);

    Product updateWithAvatar(ProductUpdateReqDTO productUpdateReqDTO,MultipartFile file, Category category, Unit unit);

    Product updateNoAvatar(ProductUpdateReqDTO productUpdateReqDTO, Optional<Product> productOptional, Category category, Unit unit);

    Product saveWithAvatar(Product product, MultipartFile file);

    ProductCountDTO countProduct();
}
