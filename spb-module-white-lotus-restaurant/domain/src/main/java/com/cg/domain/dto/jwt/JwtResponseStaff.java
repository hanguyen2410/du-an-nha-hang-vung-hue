package com.cg.domain.dto.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Getter
@Setter
@NoArgsConstructor
public class JwtResponseStaff {
    private String id;
    private String token;
    private String type = "Bearer";
    private String username;
    private String fullName;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponseStaff(String accessToken, String id, String username, String fullName, Collection<? extends GrantedAuthority> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", name='" + fullName + '\'' +
                ", roles=" + roles +
                '}';
    }
}
