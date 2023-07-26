package com.cg.service.staffAvatar;

import com.cg.domain.entity.staff.Staff;
import com.cg.domain.entity.staff.StaffAvatar;
import com.cg.repository.StaffAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class StaffAvatarServiceImpl implements IStaffAvatarService {

    @Autowired
    private StaffAvatarRepository staffAvatarRepository;


    @Override
    public List<StaffAvatar> findAll() {
        return null;
    }

    @Override
    public StaffAvatar getById(String id) {
        return null;
    }

    @Override
    public Optional<StaffAvatar> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<StaffAvatar> findByStaff(Staff staff) {
        return Optional.empty();
    }

    @Override
    public StaffAvatar save(StaffAvatar staffAvatar) {
      return   staffAvatarRepository.save(staffAvatar) ;
    }

    @Override
    public void delete(StaffAvatar staffAvatar) {

    }

    @Override
    public void deleteById(String id) {

    }
}
