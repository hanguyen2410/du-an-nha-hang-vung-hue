package com.cg.domain.dto.staff;

import com.cg.domain.dto.locationRegion.LocationRegionDTO;
import com.cg.domain.dto.role.RoleDTO;
import com.cg.domain.entity.staff.Staff;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffDTO {
    private Long id;
    private String fullName;
    private String username;
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String phone;
    private LocationRegionDTO locationRegion;
    private RoleDTO role;
    private String avatarId;
    private String fileName;
    private String fileFolder;
    private String fileUrl;

    public Staff toStaff() {
        return new Staff()
                .setId(id)
                .setFullName(fullName)
                .setPhone(phone)
                .setDob(dob)
                .setLocationRegion(locationRegion.toLocationRegion())
                ;
    }
}
