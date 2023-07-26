package com.cg.repository;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT NEW com.cg.domain.dto.category.CategoryDTO (" +
                "cat.id, " +
                "cat.title" +
            ")" +
            "FROM Category AS cat "
    )
    List<CategoryDTO> findAllCategoryDTO();

}
