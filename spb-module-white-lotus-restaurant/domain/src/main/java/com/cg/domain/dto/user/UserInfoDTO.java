package com.cg.domain.dto.user;

import com.cg.domain.entity.Role;
import com.cg.domain.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoDTO {

    private String username;
    private String fullName;
    private ERole roleName;
    private String roleCode;


    public UserInfoDTO(String username, String fullName, Role role) {
        this.username = username;
        this.fullName = fullName;
        this.roleName = role.toRoleDTO().getName();
        this.roleCode = role.toRoleDTO().getCode();
    }
}
