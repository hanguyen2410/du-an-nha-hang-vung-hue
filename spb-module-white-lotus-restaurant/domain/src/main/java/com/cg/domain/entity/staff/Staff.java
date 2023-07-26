package com.cg.domain.entity.staff;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.BaseEntity;
import com.cg.domain.entity.LocationRegion;
import com.cg.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "staffs")
public class Staff extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private Date dob;

    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_region_id", referencedColumnName = "id", nullable = false)
    private LocationRegion locationRegion;

    @ManyToOne
    @JoinColumn(name = "staff_avatar_id", referencedColumnName = "id")
    private StaffAvatar staffAvatar;


    public StaffDTO toStaffDTO () {
        return new StaffDTO()
                .setId(id)
                .setFullName(fullName)
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setRole(user.getRole().toRoleDTO())
                .setPhone(phone)
                .setDob(dob)
                .setLocationRegion(locationRegion.toLocationRegionDTO())
                .setAvatarId(staffAvatar.getId())
                .setFileFolder(staffAvatar.getFileFolder())
                .setFileUrl(staffAvatar.getFileUrl())
                .setFileName(staffAvatar.getFileName())
                ;
    }
}
