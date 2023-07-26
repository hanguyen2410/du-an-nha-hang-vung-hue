package com.cg.service.role;

import com.cg.domain.entity.Role;
import com.cg.service.IGeneralService;

import java.util.Optional;


public interface IRoleService extends IGeneralService<Role, Long> {

    Optional<Role> findByCode(String code);

}

