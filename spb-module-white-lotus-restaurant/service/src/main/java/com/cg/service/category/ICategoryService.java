package com.cg.service.category;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.entity.category.Category;
import com.cg.service.IGeneralService;

import java.util.List;

public interface ICategoryService extends IGeneralService<Category, Long> {
    List<CategoryDTO> findAllCategoryDTO();
}
