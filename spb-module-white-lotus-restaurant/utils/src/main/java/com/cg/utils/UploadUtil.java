package com.cg.utils;


import com.cg.domain.entity.staff.StaffAvatar;
import com.cg.domain.entity.product.Product;
import com.cg.domain.entity.product.ProductAvatar;
import com.cg.exception.DataInputException;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;


import java.util.Map;

@Component
public class UploadUtil {
    public static final String STAFF_IMAGE_UPLOAD_FOLDER = "White-Lotus-Restaurant/staff";
    public static final String PRODUCT_IMAGE_UPLOAD_FOLDER = "White-Lotus-Restaurant/product";
    public static final String CUSTOMER_IMAGE_UPLOAD_FOLDER = "White-Lotus-Restaurant/customer";


    public Map buildImageUploadParams(ProductAvatar productAvatar) {
        if (productAvatar == null || productAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", PRODUCT_IMAGE_UPLOAD_FOLDER, productAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

    public Map buildImageUploadParamsStaff(StaffAvatar staffAvatar) {
        if (staffAvatar == null || staffAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", STAFF_IMAGE_UPLOAD_FOLDER, staffAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

    public Map buildImageDestroyParams(Product product, String publicId) {
        if (product == null || product.getId() == null)
            throw new DataInputException("Không thể destroy hình ảnh của sản phẩm không xác định");

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }


}