package com.cg.repository;

import com.cg.domain.entity.staff.StaffAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StaffAvatarRepository extends JpaRepository<StaffAvatar, String> {

}
