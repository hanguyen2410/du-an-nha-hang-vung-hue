package com.cg.service.staff;

import com.cg.domain.dto.product.ProductCountDTO;
import com.cg.domain.dto.staff.StaffCountDTO;
import com.cg.domain.dto.staff.StaffCreateReqDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.staff.StaffFilterReqDTO;
import com.cg.domain.entity.LocationRegion;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.User;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface IStaffService extends IGeneralService<Staff, Long> {

    Optional<Staff> findByUser(User user);

    String getFullNameByUsername(String username);

    void softDelete(Long staffId);

    Page<StaffDTO> findAll(StaffFilterReqDTO staffFilterReqDTO, Pageable pageable);

    Staff createWithAvatar(StaffCreateReqDTO staffCreateDTO, LocationRegion locationRegion, User user, MultipartFile file);

    Staff createNoAvatar(StaffCreateReqDTO staffCreateDTO, LocationRegion locationRegion, User user);

    Optional<Staff> findByPhone(String phone);

    Staff saveNoAvatar(Staff staff);

    Staff saveWithAvatar(Staff staff, MultipartFile file);

    Boolean existsByPhoneAndIdNot(String phone, Long id);

    Optional<Staff> findByUsername(String username);

    StaffCountDTO countStaff();

}
