package com.cg.domain.dto.user;

import com.cg.domain.dto.role.RoleDTO;
import com.cg.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreateReqDTO {

    private String id;

    @Pattern(regexp = "^[\\w]+@([\\w-]+\\.)+[\\w-]{2,6}$", message = "Email không hợp lệ!")
    @Size(min = 8, max = 35, message = "Độ dài email nằm trong khoảng 8-35 ký tự!")
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu!")
    @Size(min = 3, max = 50, message = "Độ dài mật khẩu nằm trong khoảng 3-50 ký tự!")
    private String password;

    @Valid
    private RoleDTO role;

    public UserCreateReqDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public User toUser() {
        return new User()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setRole(role.toRole());
    }
}
