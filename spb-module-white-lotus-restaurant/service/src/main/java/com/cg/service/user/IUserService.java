package com.cg.service.user;

import com.cg.domain.dto.user.UserInfoDTO;
import com.cg.domain.entity.User;
import com.cg.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;

import java.util.Optional;

public interface IUserService extends IGeneralService<User, String>, UserDetailsService {

    UserInfoDTO getInfo(Model model);

    Boolean existsUserByUsernameAndIdNot(String username, String id);

    Boolean existsStaffByUsername(String username);

    Optional<User> findByUsername(String username);

    User getByUsername(String username);

    UserInfoDTO getInfoByUsername(String username);
    void softDelete(String userId);

}
