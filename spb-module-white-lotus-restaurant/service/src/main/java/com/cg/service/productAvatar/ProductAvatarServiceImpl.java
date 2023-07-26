package com.cg.service.productAvatar;

import com.cg.domain.entity.product.ProductAvatar;
import com.cg.repository.ProductAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProductAvatarServiceImpl implements IProductAvatarService {
    @Autowired
    private ProductAvatarRepository productAvatarRepository;

    @Override
    public List<ProductAvatar> findAll() {
        return null;
    }

    @Override
    public ProductAvatar getById(String id) {
        return null;
    }

    @Override
    public Optional<ProductAvatar> findById(String id) {
        return productAvatarRepository.findById(id);
    }

    @Override
    public ProductAvatar save(ProductAvatar productAvatar) {
        return productAvatarRepository.save(productAvatar);
    }

    @Override
    public void delete(ProductAvatar productAvatar) {

    }

    @Override
    public void deleteById(String id) {

    }

}
