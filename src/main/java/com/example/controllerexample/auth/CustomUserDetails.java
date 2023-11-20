package com.example.controllerexample.auth;

import com.example.controllerexample.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails extends User implements UserDetails {

    public CustomUserDetails(String username, String nickname, String password) {
        super(username, nickname, password);
    }

    public CustomUserDetails(Long id, String username, String nickname, String password) {
        super(id, username, nickname, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
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
    public String toString() {
        return "CustomUserDetails{ authorities: " + getAuthorities().toString() + ", " + super.toString();
    }
}
