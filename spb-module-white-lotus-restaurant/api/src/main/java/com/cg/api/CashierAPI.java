package com.cg.api;

import com.cg.domain.dto.bill.BillGetTwoDayDTO;
import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.product.ProductResDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.service.bill.IBillService;
import com.cg.service.category.ICategoryService;
import com.cg.service.product.IProductService;
import com.cg.service.table.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cashiers")
public class CashierAPI {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private ITableService tableService;

    @Autowired
    private IBillService iBillService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProductResDTO> productDTOS = iProductService.findAllProductResDTO();
        List<CategoryDTO> categoryDTOS = iCategoryService.findAllCategoryDTO();
        List<TableDTO> tableDTOs= tableService.getAllTableDTOWhereDeletedIsFalse();

        Map<String, Object> result = new HashMap<String,Object>();
        result.put("products", productDTOS);
        result.put("categories", categoryDTOS);
        result.put("tables", tableDTOs);

        if (productDTOS.isEmpty() || categoryDTOS.isEmpty() || tableDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-all-tables-and-categories")
    public ResponseEntity<?> getAllTablesAndCategories() {
        List<CategoryDTO> categoryDTOS = iCategoryService.findAllCategoryDTO();
        List<TableDTO> tableDTOs= tableService.getAllTableDTOWhereDeletedIsFalse();

        Map<String, Object> result = new HashMap<String,Object>();
        result.put("categories", categoryDTOS);
        result.put("tables", tableDTOs);

        if (categoryDTOS.isEmpty() || tableDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<?> getAllProducts() {
        List<ProductResDTO> productDTOS = iProductService.findAllProductResDTO();

        if (productDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Map<String, Object> result = new HashMap<String,Object>();
        result.put("products", productDTOS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
