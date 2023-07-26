package com.cg.repository;

import com.cg.domain.dto.product.ProductFilterReqDTO;
import com.cg.domain.dto.product.ProductResDTO;
import com.cg.domain.dto.product.ProductCountDTO;
import com.cg.domain.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT NEW com.cg.domain.dto.product.ProductResDTO ( " +
                "pro.id, " +
                "pro.title, " +
                "pro.price, " +
                "pro.unit, " +
                "pro.cooking, " +
                "pro.category, " +
                "pro.description, " +
                "pro.productAvatar, " +
                "pro.outStock " +
            ")" +
            "FROM Product AS pro " +
            "JOIN Category AS cate " +
            "ON pro.category = cate " +
            "JOIN Unit AS un " +
            "ON pro.unit = un " +
            "JOIN ProductAvatar AS ava " +
            "ON pro.productAvatar = ava " +
            "WHERE pro.deleted = false "
    )
    List<ProductResDTO> findAllProductResDTO();

    @Query("SELECT NEW com.cg.domain.dto.product.ProductResDTO ( " +
                "pro.id," +
                "pro.title," +
                "pro.price," +
                "pro.unit," +
                "pro.cooking, " +
                "pro.category," +
                "pro.description," +
                "pro.productAvatar, " +
                "pro.outStock " +
            ")" +
            "FROM Product AS pro " +
            "JOIN Category AS cate " +
            "ON pro.category = cate " +
            "JOIN Unit AS un " +
            "ON pro.unit = un " +
            "JOIN ProductAvatar AS ava " +
            "ON pro.productAvatar = ava " +
            "WHERE pro.deleted = false " +
            "AND pro.title like :keySearch "
    )
    List<ProductResDTO> findAllProductResDTOBySearch(String keySearch);

    @Query("SELECT NEW com.cg.domain.dto.product.ProductResDTO ( " +
                "pro.id," +
                "pro.title," +
                "pro.price," +
                "pro.unit," +
                "pro.cooking, " +
                "pro.category," +
                "pro.description," +
                "pro.productAvatar, " +
                "pro.outStock " +
            ")" +
            "FROM Product AS pro " +
            "JOIN Category AS cate " +
            "ON pro.category = cate " +
            "JOIN Unit AS un " +
            "ON pro.unit = un " +
            "JOIN ProductAvatar AS ava " +
            "ON pro.productAvatar = ava " +
            "WHERE pro.deleted = false " +
            "AND pro.category.id = :categoryId"
    )
    List<ProductResDTO> findAllProductResDTOByCategoryId(@Param("categoryId") Long categoryId );

    @Query("SELECT NEW com.cg.domain.dto.product.ProductResDTO ( " +
                "pro.id," +
                "pro.title," +
                "pro.price," +
                "pro.unit," +
                "pro.cooking, " +
                "pro.category," +
                "pro.description," +
                "pro.productAvatar, " +
                "pro.outStock " +
            ")" +
            "FROM Product AS pro " +
            "JOIN Category AS cate " +
            "ON pro.category = cate " +
            "JOIN Unit AS un " +
            "ON pro.unit = un " +
            "JOIN ProductAvatar AS ava " +
            "ON pro.productAvatar = ava " +
            "JOIN OrderItem AS oi " +
            "ON pro.id = oi.product.id " +
            "WHERE pro.deleted = false " +
            "GROUP BY pro.id " +
            "ORDER BY SUM(oi.quantity) DESC"
    )
    List<ProductResDTO> getTop10SalesProductLastMonth(Pageable pageable);


    default Page<Product> findAll(ProductFilterReqDTO productFilterReqDTO, Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String keyWord = productFilterReqDTO.getKeyWord();
            Integer categoryFilter = productFilterReqDTO.getCategoryFilter();
            String unitFilter = productFilterReqDTO.getUnitFilter();

            if (keyWord != null) {
                Predicate predicateNameProduct = criteriaBuilder.like(root.get("title"), '%' + keyWord + '%');
                Predicate predicateDescription = criteriaBuilder.like(root.get("description"), '%' + keyWord + '%');
                Predicate predicateKw = criteriaBuilder.or(predicateNameProduct, predicateDescription);
                predicates.add(predicateKw);
            }

            if (categoryFilter != null && categoryFilter != -1) {
                Predicate predicate = criteriaBuilder.equal(root.get("category").get("id"), categoryFilter);
                predicates.add(predicate);
            }

            if (unitFilter != null) {
                Predicate predicate = criteriaBuilder.like(root.get("unit"), '%' + unitFilter + '%');
                predicates.add(predicate);
            }
            Predicate predicate = criteriaBuilder.equal(root.get("deleted"), false);
            predicates.add(predicate);

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

    }

    boolean existsByTitle(String title);

    @Query("SELECT NEW com.cg.domain.dto.product.ProductCountDTO (" +
            "count(pr.id) " +
            ") " +
            "FROM Product AS pr " +
            "WHERE pr.deleted = false "
    )
    ProductCountDTO countProduct ();

}
