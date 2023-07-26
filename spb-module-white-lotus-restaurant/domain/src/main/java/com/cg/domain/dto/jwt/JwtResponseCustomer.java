package com.cg.domain.dto.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Getter
@Setter
@NoArgsConstructor
public class JwtResponseCustomer {

    private String id;
    private String token;
    private String type = "Bearer";
    private String fullName;
    private String email;
    private String phone;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponseCustomer(String accessToken, String id, String fullName, String email, String phone, Collection<? extends GrantedAuthority> roles) {
        this.token = accessToken;
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "JwtResponseCustomer{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
