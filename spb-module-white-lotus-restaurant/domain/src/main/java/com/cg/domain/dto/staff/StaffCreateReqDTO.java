package com.cg.domain.dto.staff;

import com.cg.domain.entity.*;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.staff.StaffAvatar;
import com.cg.exception.DataInputException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.text.SimpleDateFormat;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffCreateReqDTO {
    private Long id;

    @NotEmpty(message = "Tên nhân viên không được để trống")
    @Size(min = 3, max = 30, message = "Độ dài tên nằm trong khoảng 3-30 ký tự!")
    private String fullName;

    @NotEmpty(message = "Vui lòng nhập ngày tháng năm sinh!")
    @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Ngày sinh không đúng định dạng.")
    private String dob;

    @NotEmpty(message = "Vui lòng nhập số điện thoại!")
    @Pattern(regexp = "^0[1-9][0-9]{8}$", message = "Số điện thoại không đúng định dạng.")
    private String phone;

    @Pattern(regexp = "^\\d+$", message = "ID Tỉnh/Thành phố phải là số.")
    @NotEmpty(message = "ID Tỉnh/Thành phố xã không được trống.")
    private String provinceId;

    @NotEmpty(message = "Tên Tỉnh/Thành phố không được trống.")
    private String provinceName;

    @Pattern(regexp = "^\\d+$", message = "ID Thành phố/Quận/Huyện phải là số.")
    @NotEmpty(message = "ID Thành phố/Quận/Huyện xã không được trống.")
    private String districtId;

    @NotEmpty(message = "Tên Thành phố/Quận/Huyện xã không được trống.")
    private String districtName;

    @Pattern(regexp = "^\\d+$", message = "ID Phường/Xã/Thị trấn phải là số.")
    @NotEmpty(message = "Phường/Xã/Thị trấn không được trống.")
    private String wardId;

    @NotEmpty(message = "Tên Phường/Xã/Thị trấn không được trống.")
    private String wardName;

    @NotEmpty(message = "Vui lòng nhập địa chỉ")
    @Size(min = 5, max = 100, message = "Địa chỉ có độ dài nằm trong khoảng 5 - 100 ký tự.")
    private String address;

    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email không đúng định dạng.")
    @NotEmpty(message = "Vui lòng nhập email!")
    private String username;

    private String password;

    @Pattern(regexp = "^\\d+$", message = "ID role phải là số.")
    @NotEmpty(message = "ID role không được trống.")
    private String roleId;

    private MultipartFile file;

    public LocationRegion getLocation(){
        return new LocationRegion()
                .setId(id)
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address);
    }

    public User toUser(Role role) {
        return new User()
                .setId(null)
                .setUsername(username)
                .setPassword(password)
                .setRole(role);
    }

    public Staff toStaff(User user, LocationRegion locationRegion, StaffAvatar staffAvatar) {
        try {
            return new Staff()
                    .setId(id)
                    .setFullName(fullName)
                    .setPhone(phone)
                    .setDob(new SimpleDateFormat("yyyy-MM-dd").parse(dob))
                    .setUser(user)
                    .setLocationRegion(locationRegion)
                    .setStaffAvatar(staffAvatar);

        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ");
        }
    }

}
