package com.cg.service.staffAvatar;

import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.staff.StaffAvatar;
import com.cg.service.IGeneralService;


import java.util.Optional;

public interface IStaffAvatarService extends IGeneralService<StaffAvatar, String> {

    Optional<StaffAvatar> findByStaff(Staff staff);
}
