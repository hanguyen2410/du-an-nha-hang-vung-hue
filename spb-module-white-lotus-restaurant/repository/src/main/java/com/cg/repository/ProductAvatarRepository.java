package com.cg.repository;

import com.cg.domain.entity.product.ProductAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAvatarRepository extends JpaRepository<ProductAvatar,String> {
}
