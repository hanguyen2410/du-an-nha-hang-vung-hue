package com.cg.service.product;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.product.*;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.product.ProductAvatar;
import com.cg.domain.entity.unit.Unit;
import com.cg.domain.enums.EFileType;
import com.cg.exception.DataInputException;
import com.cg.repository.ProductRepository;
import com.cg.service.productAvatar.ProductAvatarServiceImpl;
import com.cg.service.upload.IUploadService;
import com.cg.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@ConfigurationProperties(prefix = "application.cloudinary")
@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAvatarServiceImpl productAvatarService;

    @Autowired
    private IUploadService iUploadService;

    @Autowired
    private UploadUtil uploadUtil;

    @Value("${application.cloudinary.server-name}")
    private String cloudinaryServerName;

    @Value("${application.cloudinary.cloud-name}")
    private String cloudinaryName;

    @Value("${application.cloudinary.default-folder}")
    private String cloudinaryDefaultFolder;

    @Value("${application.cloudinary.default-file-name}")
    private String cloudinaryDefaultFileName;

    @Override
    public Page<ProductResDTO> findAll(ProductFilterReqDTO productFilterReqDTO, Pageable pageable) {
//        return productRepository.findAll(productFilterReqDTO, pageable).map(Product::toProductResDTO);
        return productRepository.findAll(productFilterReqDTO, pageable).map(Product::toProductResDTO);
    }

    @Override
    public List<ProductResDTO> findAllProductResDTO() {
        return productRepository.findAllProductResDTO();
    }

    @Override
    public List<ProductResDTO> findAllProductResDTOBySearch(String keySearch) {
        return productRepository.findAllProductResDTOBySearch(keySearch);
    }

    @Override
    public List<ProductResDTO> findAllProductResDTOByCategoryId(Long categoryId) {
        return productRepository.findAllProductResDTOByCategoryId(categoryId);
    }

    @Override
    public List<ProductResDTO> getTop10SalesProductLastMonth() {
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.getTop10SalesProductLastMonth(pageable);
    }

    @Override
    public boolean existsByTitle(String title) {
        return productRepository.existsByTitle(title);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product createWithAvatar(ProductCreateReqDTO productCreateReqDTO, MultipartFile file, Category category, Unit unit) {

        String fileType = file.getContentType();
        assert fileType != null;
        fileType = fileType.substring(0, 5);

        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar.setFileType(fileType);
        productAvatar = productAvatarService.save(productAvatar);

        if (fileType.equals(EFileType.IMAGE.getValue())) {
            productAvatar = uploadAndSaveProductAvatar(file, productAvatar);
        }

        Product product = productCreateReqDTO.toProduct(productAvatar, category, unit);
        product = productRepository.save(product);
        return product;
    }

    @Override
    public Product createNoAvatar(ProductCreateReqDTO productCreateReqDTO, Category category, Unit unit) {

        String cloudId = cloudinaryDefaultFolder + "/" + cloudinaryDefaultFileName;
        String fileUrl = cloudinaryServerName + "/" + cloudinaryName + "/image/upload/" + cloudId;

        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar.setFileType(EFileType.IMAGE.getValue());
        productAvatar.setFileFolder(cloudinaryDefaultFolder);
        productAvatar.setFileName(cloudinaryDefaultFileName);
        productAvatar.setCloudId(cloudId);
        productAvatar.setFileUrl(fileUrl);

        productAvatar = productAvatarService.save(productAvatar);

        Product product = productCreateReqDTO.toProduct(productAvatar, category, unit);
        product = productRepository.save(product);
        return product;
    }

    @Override
    public Product updateWithAvatar(ProductUpdateReqDTO productUpdateReqDTO, MultipartFile file, Category category, Unit unit) {
        String fileType = file.getContentType();
        assert fileType != null;
        fileType = fileType.substring(0, 5);

        ProductAvatar productAvatar = new ProductAvatar();
        productAvatar.setFileType(fileType);
        productAvatar = productAvatarService.save(productAvatar);

        if (fileType.equals(EFileType.IMAGE.getValue())) {
            productAvatar = uploadAndSaveProductAvatar(file, productAvatar);
        }

        Product product = productUpdateReqDTO.toProduct(productAvatar, category, unit);
        product = productRepository.save(product);
        return product;
    }

    @Override
    public Product updateNoAvatar(ProductUpdateReqDTO productUpdateReqDTO, Optional<Product> productOptional, Category category, Unit unit) {

        String avatarId = productOptional.get().getProductAvatar().getId();

        Optional<ProductAvatar> productAvatarOp = productAvatarService.findById(avatarId);

        ProductAvatar productAvatar = productAvatarOp.get();

        productAvatar = productAvatarService.save(productAvatar);

        Product product = productUpdateReqDTO.toProduct(productAvatar, category, unit);
        product = productRepository.save(product);
        return product;
    }

    @Override
    public Product saveWithAvatar(Product product, MultipartFile file) {
        ProductAvatar oldProductAvatar = product.getProductAvatar();
        try {
            iUploadService.destroyImage(oldProductAvatar.getCloudId(), uploadUtil.buildImageDestroyParams(product, oldProductAvatar.getCloudId()));
            productAvatarService.deleteById(oldProductAvatar.getId());

            String fileType = file.getContentType();
            assert fileType != null;
            fileType = fileType.substring(0, 5);

            ProductAvatar productAvatar = new ProductAvatar();
            productAvatar.setFileType(fileType);
            productAvatar = productAvatarService.save(productAvatar);

            if (fileType.equals(EFileType.IMAGE.getValue())) {
                productAvatar = uploadAndSaveProductAvatar(file, productAvatar);
            }

            product.setProductAvatar(productAvatar);
            product = productRepository.save(product);

            return product;
        } catch (IOException e) {
            throw new DataInputException("Xóa hình ảnh thất bại.");
        }
    }

    private ProductAvatar uploadAndSaveProductAvatar(MultipartFile file, ProductAvatar productAvatar) {
        try {
            Map uploadResult = iUploadService.uploadImage(file, uploadUtil.buildImageUploadParams(productAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");
            Integer width = (Integer) uploadResult.get("width");
            Integer height = (Integer) uploadResult.get("height");

            productAvatar.setFileName(productAvatar.getId() + "." + fileFormat);
            productAvatar.setFileUrl(fileUrl);
            productAvatar.setFileFolder(UploadUtil.PRODUCT_IMAGE_UPLOAD_FOLDER);
            productAvatar.setCloudId(productAvatar.getFileFolder() + "/" + productAvatar.getId());
            productAvatar.setWidth(width);
            productAvatar.setHeight(height);
            return productAvatarService.save(productAvatar);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại.");
        }
    }

    @Override
    public void delete(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public ProductCountDTO countProduct() {
        return productRepository.countProduct();
    }
}
