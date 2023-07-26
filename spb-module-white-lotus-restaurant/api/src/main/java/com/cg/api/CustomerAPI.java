package com.cg.api;


import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.product.ProductResDTO;
import com.cg.domain.entity.product.Product;
import com.cg.exception.DataInputException;
import com.cg.service.category.ICategoryService;
import com.cg.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    @Autowired
    IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;


    @GetMapping("/get-all-products/{categoryId}")
    public ResponseEntity<?> getAllProductFoods(@PathVariable Long categoryId) {

        if(categoryId == 0){
            List<ProductResDTO> productDTOS = iProductService.findAllProductResDTO();
            if (productDTOS.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productDTOS, HttpStatus.OK);
        }
        List<ProductResDTO> productDTOS = iProductService.findAllProductResDTOByCategoryId(categoryId);
        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }
    @GetMapping("/get-products-by-id/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {

        Optional<Product> optionalProduct = iProductService.findById(productId);

        if (!optionalProduct.isPresent()) {
            throw new DataInputException("ID hàng hóa không hợp lệ !");
        }

        Product product = optionalProduct.get();
        ProductResDTO productResDTO = product.toProductResDTO();
        return new ResponseEntity<>(productResDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllProductBySearch(@RequestParam String keySearch) {
        keySearch = '%' + keySearch + '%';
        List<ProductResDTO> productResDTOS = iProductService.findAllProductResDTOBySearch(keySearch);
        if (productResDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productResDTOS, HttpStatus.OK);
    }


    @GetMapping("get-best-sale")
    public ResponseEntity<?> getProductBestSale() {

        List<ProductResDTO> productDTOS = iProductService.getTop10SalesProductLastMonth();
        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    


    @GetMapping("/category")
    public ResponseEntity<?> getAllCategories() {

        List<CategoryDTO> categoryDTOS = iCategoryService.findAllCategoryDTO();
        if (categoryDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }

}
