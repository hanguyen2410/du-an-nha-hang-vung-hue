package com.cg.service.staff;


import com.cg.domain.dto.staff.StaffCountDTO;
import com.cg.domain.dto.staff.StaffCreateReqDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.staff.StaffFilterReqDTO;
import com.cg.domain.entity.*;
import com.cg.domain.entity.product.ProductAvatar;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.staff.StaffAvatar;
import com.cg.domain.enums.EFileType;
import com.cg.exception.DataInputException;
import com.cg.repository.*;
import com.cg.service.locationRegion.ILocationRegionService;
import com.cg.service.staffAvatar.StaffAvatarServiceImpl;
import com.cg.service.upload.IUploadService;
import com.cg.service.user.IUserService;
import com.cg.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class StaffServiceImpl implements IStaffService {


    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffAvatarServiceImpl staffAvatarService;

    @Autowired
    private ILocationRegionService locationRegionService;

    @Autowired
    private IUserService userService;

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
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    public Staff getById(Long id) {
        return staffRepository.getById(id);
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    public Optional<Staff> findByPhone(String phone) {
        return staffRepository.findByPhone(phone);
    }

    @Override
    public Optional<Staff> findByUser(User user) {
        return staffRepository.findByUser(user);
    }

    @Override
    public String getFullNameByUsername(String username) {
        return staffRepository.getFullNameByUsername(username);
    }

    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public void softDelete(Long staffId) {
        staffRepository.softDelete(staffId);
    }

    @Override
    public void delete(Staff staff) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Page<StaffDTO> findAll(StaffFilterReqDTO staffFilterReqDTO, Pageable pageable) {
//        return staffRepository.findAll(staffFilterReqDTO, pageable).map(e -> e.toStaffDTO(e));
        return staffRepository.findAll(staffFilterReqDTO, pageable).map(Staff::toStaffDTO);
    }

    private StaffAvatar uploadAndSaveStaffAvatar(MultipartFile file, StaffAvatar staffAvatar) {
        try {
            Map uploadResult = iUploadService.uploadImage(file, uploadUtil.buildImageUploadParamsStaff(staffAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            staffAvatar.setFileName(staffAvatar.getId() + "." + fileFormat);
            staffAvatar.setFileUrl(fileUrl);
            staffAvatar.setFileFolder(UploadUtil.PRODUCT_IMAGE_UPLOAD_FOLDER);
            staffAvatar.setCloudId(staffAvatar.getFileFolder() + "/" + staffAvatar.getId());
            return staffAvatarService.save(staffAvatar);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại.");
        }
    }

    @Override
    public Staff createWithAvatar(StaffCreateReqDTO staffCreateDTO, LocationRegion locationRegion, User user, MultipartFile file) {

        locationRegion = locationRegionService.save(locationRegion);
        user = userService.save(user);

        String fileType = file.getContentType();
        assert fileType != null;
        fileType = fileType.substring(0, 5);
        StaffAvatar staffAvatar = new StaffAvatar();
        staffAvatar.setFileType(fileType);
        staffAvatar = staffAvatarService.save(staffAvatar);


        if (fileType.equals(EFileType.IMAGE.getValue())) {
            staffAvatar = uploadAndSaveStaffAvatar(file, staffAvatar);
        }


        Staff staff  = staffCreateDTO.toStaff(user, locationRegion, staffAvatar);
        staff.setId(null);

        staff = staffRepository.save(staff);

        return staff;
    }

    @Override
    public Staff createNoAvatar(StaffCreateReqDTO staffCreateDTO, LocationRegion locationRegion, User user) {

        locationRegion = locationRegionService.save(locationRegion);
        user = userService.save(user);

        String cloudId = cloudinaryDefaultFolder + "/" + cloudinaryDefaultFileName;
        String fileUrl = "res.cloudinary.com/cloudinarymen/image/upload/v1683164385/White-Lotus-Restaurant/images_cummji.jpg";

        StaffAvatar staffAvatar = new StaffAvatar();
        staffAvatar.setFileType(EFileType.IMAGE.getValue());
        staffAvatar.setFileFolder(cloudinaryDefaultFolder);
        staffAvatar.setFileName(cloudinaryDefaultFileName);
        staffAvatar.setCloudId(cloudId);
        staffAvatar.setFileUrl(fileUrl);

        staffAvatar = staffAvatarService.save(staffAvatar);

        Staff staff  = staffCreateDTO.toStaff(user, locationRegion, staffAvatar);
        staff.setId(null);

        staff = staffRepository.save(staff);

        return staff;
    }

    @Override
    public Staff saveNoAvatar(Staff staff){

        userService.save(staff.getUser());
        locationRegionService.save(staff.getLocationRegion());

        return staffRepository.save(staff);
    }

    @Override
    public Staff saveWithAvatar(Staff staff, MultipartFile file) {

        String fileType = file.getContentType();
        assert fileType != null;
        fileType = fileType.substring(0, 5);
        StaffAvatar staffAvatar = new StaffAvatar();
        staffAvatar.setFileType(fileType);

        staffAvatar = staffAvatarService.save(staffAvatar);

        if (fileType.equals(EFileType.IMAGE.getValue())) {
            staffAvatar = uploadAndSaveStaffAvatar(file, staffAvatar);
        }

        staff.setStaffAvatar(staffAvatar);
        staff = staffRepository.save(staff);

        return staff;
    }

    @Override
    public Boolean existsByPhoneAndIdNot(String phone, Long id) {
        return staffRepository.existsByPhoneAndIdNot(phone, id);
    }

    @Override
    public Optional<Staff> findByUsername(String username) {
        return staffRepository.findByUsername(username);
    }

    @Override
    public StaffCountDTO countStaff() {
        return staffRepository.countStaff();
    }
}
