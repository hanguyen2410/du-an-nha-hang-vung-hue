package com.cg.api;

import com.cg.domain.dto.staff.StaffCreateReqDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.staff.StaffFilterReqDTO;
import com.cg.domain.dto.staff.StaffUpdateReqDTO;
import com.cg.domain.entity.LocationRegion;
import com.cg.domain.entity.Role;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.User;
import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.service.role.IRoleService;
import com.cg.service.staff.IStaffService;
import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Optional;


@RestController
@RequestMapping("/api/staffs")
public class    StaffAPI {
    @Autowired
    private IStaffService staffService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @PostMapping("/get-all")
    public ResponseEntity<?> getAllStaffs(@RequestBody StaffFilterReqDTO staffFilterReqDTO, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size =5) Pageable pageable) {
        int size = 5;
        int currentPageNumber = staffFilterReqDTO.getCurrentPageNumber();
        pageable = PageRequest.of(currentPageNumber, size, Sort.by("id").ascending());
        Page<StaffDTO> staffDTOS = staffService.findAll(staffFilterReqDTO, pageable);
        if (staffDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(staffDTOS, HttpStatus.OK);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<?> getById(@PathVariable Long staffId) {

        Optional<Staff> optionalStaff = staffService.findById(staffId);

        if (!optionalStaff.isPresent()) {
            throw new DataInputException("ID Nhân viên không hợp lệ");
        }

        Staff staff = optionalStaff.get();
        StaffDTO staffDTO = staff.toStaffDTO();
        return new ResponseEntity<>(staffDTO, HttpStatus.OK);
    }

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createStaff(MultipartFile file, @Validated StaffCreateReqDTO staffCreateDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<User> userOptional = userService.findByUsername(staffCreateDTO.getUsername());

        if (userOptional.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại trong hệ thống.");
        }

        Optional<Staff> staffOptional = staffService.findByPhone(staffCreateDTO.getPhone());

        if (staffOptional.isPresent()) {
            throw new DataInputException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        LocationRegion locationRegion = staffCreateDTO.getLocation();
        locationRegion.setId(null);

        Optional<Role> optRole = roleService.findById(Long.parseLong(staffCreateDTO.getRoleId()));

        if (!optRole.isPresent()) {
            throw new DataInputException("Role không hợp lệ.");
        }

        Role role = optRole.get();

        User user = staffCreateDTO.toUser(role);

        user.setId(null);

        Staff newStaff;

        if (file == null) {
            newStaff = staffService.createNoAvatar(staffCreateDTO, locationRegion, user);
        } else {
            newStaff = staffService.createWithAvatar(staffCreateDTO, locationRegion, user, file);
        }
        return new ResponseEntity<>(newStaff.toStaffDTO(), HttpStatus.CREATED);
    }

    @PatchMapping("/{staffId}")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updateStaff(@PathVariable Long staffId, MultipartFile file, @Validated StaffUpdateReqDTO staffUpdateDTO, BindingResult bindingResult) {

        Optional<Staff> staffOptional = staffService.findById(staffId);
        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không tồn tại.");
        }

        Staff staff = staffOptional.get();

        String phone = staffUpdateDTO.getPhone();

        if (staffService.existsByPhoneAndIdNot(phone, staffId)) {
            throw new DataInputException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        long roleId;

        try {
            roleId = Long.parseLong(staffUpdateDTO.getRoleId());
        } catch (Exception e) {
            throw new DataInputException("Role không hợp lệ.");
        }

        Optional<Role> roleOptional = roleService.findById(roleId);

        if (!roleOptional.isPresent()) {
            throw new DataInputException("Role không tồn tại.");
        }

        Role role = roleOptional.get();

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        LocationRegion newLocationRegion = staffUpdateDTO.toLocationRegion();
        try {
            staff.setFullName(staffUpdateDTO.getFullName())
                    .setDob(new SimpleDateFormat("yyyy-MM-dd").parse(staffUpdateDTO.getDob()))
                    .setPhone(phone);
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ");
        }

        staff.getUser().setRole(role);

        staff.getLocationRegion()
                .setProvinceId(newLocationRegion.getProvinceId())
                .setProvinceName(newLocationRegion.getProvinceName())
                .setDistrictId(newLocationRegion.getDistrictId())
                .setDistrictName(newLocationRegion.getDistrictName())
                .setWardId(newLocationRegion.getWardId())
                .setWardName(newLocationRegion.getWardName())
                .setAddress(newLocationRegion.getAddress());

        staff = staffService.saveNoAvatar(staff);

        if (file != null) {
            staff = staffService.saveWithAvatar(staff, file);
        }

        return new ResponseEntity<>(staff.toStaffDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{staffId}")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long staffId) {

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ.");
        }

        Staff staff = staffOptional.get();

        if (staff.getUser().getRole().getCode().equals("ADMIN")) {
            throw new DataInputException("Không thể xóa nhân viên là ADMIN.");
        }

        try {
            userService.softDelete(staff.getUser().getId());
            staffService.softDelete(staffId);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (Exception e) {

            throw new DataInputException("Vui lòng liên hệ Administrator.");
        }
    }


}
