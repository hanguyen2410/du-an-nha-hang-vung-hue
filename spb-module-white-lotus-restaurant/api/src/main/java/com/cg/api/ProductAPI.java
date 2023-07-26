package com.cg.api;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.product.*;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.unit.Unit;
import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.category.ICategoryService;
import com.cg.service.product.IProductService;
import com.cg.service.unit.IUnitService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IUnitService iUnitService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllProduct() {
        List<ProductResDTO> productDTOS = iProductService.findAllProductResDTO();

        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @PostMapping("/get-all")
    public ResponseEntity<?> getAllProduct(@RequestBody ProductFilterReqDTO productFilterReqDTO, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 100) Pageable pageable) {

        int size = 10;
        int currentPageNumber = productFilterReqDTO.getCurrentPageNumber();

        pageable = PageRequest.of(currentPageNumber, size, Sort.by("id").ascending());

        Page<ProductResDTO> productDTOS = iProductService.findAll(productFilterReqDTO, pageable);
        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable Long productId) {

        Optional<Product> optionalProduct = iProductService.findById(productId);

        if (!optionalProduct.isPresent()) {
            throw new DataInputException("ID hàng hóa không hợp lệ !");
        }

        Product product = optionalProduct.get();
        ProductResDTO productResDTO = product.toProductResDTO();
        return new ResponseEntity<>(productResDTO, HttpStatus.OK);
    }


    @PostMapping("/create")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> create(@Validated ProductCreateReqDTO productCreateReqDTO,
                                    MultipartFile file,
                                    BindingResult bindingResult
                                    ) {

        MultipartFile imageFile = file;

        Optional<Category> categoryOptional = iCategoryService.findById(productCreateReqDTO.getCategoryId());

        Optional<Unit> unitOptional = iUnitService.findById(productCreateReqDTO.getUnitId());

        if (!categoryOptional.isPresent()) {
            throw new DataInputException("Danh mục không hợp lệ");
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (iProductService.existsByTitle(productCreateReqDTO.getTitle())) {
            throw new EmailExistsException("Sản phẩm đã tồn tại trong hệ thống!!");
        }

        Category category = categoryOptional.get();

        Unit unit = unitOptional.get();

        productCreateReqDTO.setId(null);

        Product newProduct;

        if (imageFile == null) {
            newProduct = iProductService.createNoAvatar(productCreateReqDTO, category, unit);
        } else {
            newProduct = iProductService.createWithAvatar(productCreateReqDTO, file, category, unit);
        }


        return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long productId) {
        Optional<Product> productOptional = iProductService.findById(productId);
        if (!productOptional.isPresent()) {
            throw new ResourceNotFoundException("Product not found");
        }

        iProductService.delete(productOptional.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{productId}")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long productId,
                                    @Validated ProductUpdateReqDTO productUpdateReqDTO,
                                    MultipartFile file,
                                    BindingResult bindingResult
                                    ){

        Optional<Product> productOptional = iProductService.findById(productId);

        Optional<Category> categoryOptional = iCategoryService.findById(productUpdateReqDTO.getCategoryId());

        Optional<Unit> unitOptional = iUnitService.findById(productUpdateReqDTO.getUnitId());

        if (!categoryOptional.isPresent()) {
            throw new DataInputException("Danh mục không hợp lệ");
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Category category = categoryOptional.get();

        Unit unit = unitOptional.get();

        Product newProduct;

        productUpdateReqDTO.setId(productId);

        if (file == null) {
            newProduct = iProductService.updateNoAvatar(productUpdateReqDTO,productOptional, category, unit);
        } else {
            newProduct = iProductService.updateWithAvatar(productUpdateReqDTO, file, category, unit);
        }

        return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/set-out-stock-product/{productId}")
    public ResponseEntity<?> setOutStock(@PathVariable Long productId){

        Optional<Product> optionalProduct = iProductService.findById(productId);

        if (!optionalProduct.isPresent()) {
            throw new DataInputException("ID hàng hóa không hợp lệ !");
        }

        Product product = optionalProduct.get();
        product.setOutStock(true);
        iProductService.save(product);
        ProductResDTO productResDTO = product.toProductResDTO();
        return new ResponseEntity<>(productResDTO, HttpStatus.OK);
    }

    @PostMapping("/restore-out-stock-product/{productId}")
    public ResponseEntity<?> restoreOutStock(@PathVariable Long productId){

        Optional<Product> optionalProduct = iProductService.findById(productId);

        if (!optionalProduct.isPresent()) {
            throw new DataInputException("ID hàng hóa không hợp lệ !");
        }

        Product product = optionalProduct.get();
        product.setOutStock(false);
        iProductService.save(product);
        ProductResDTO productResDTO = product.toProductResDTO();
        return new ResponseEntity<>(productResDTO, HttpStatus.OK);
    }
}
