package com.cg.domain.entity;

import com.cg.domain.enums.ERole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final String id;

    private final String username;
    private final String fullName;

    private final String password;

    private final Collection<? extends GrantedAuthority> roles;

    private final String role;

    public UserPrinciple(String id, String username, String fullName, String password, Collection<? extends GrantedAuthority> roles, ERole role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.roles = roles;
        this.role = role.getValue();
    }

    public static UserPrinciple build(User user, String fullName) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getCode());
        authorities.add(authority);

        return new UserPrinciple(
                user.getId(),
                user.getUsername(),
                fullName,
                user.getPassword(),
                authorities,
                user.getRole().getName()
        );
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
