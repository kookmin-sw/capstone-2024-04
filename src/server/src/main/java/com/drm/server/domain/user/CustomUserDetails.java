package com.drm.server.domain.user;

import com.drm.server.common.enums.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final CustomUserInfoDto customUserInfo;

    public Authority getAuthority(){
        return customUserInfo.getAuthority();}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authorities = new ArrayList<>();
        authorities.add(customUserInfo.getAuthority().toString());
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return customUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return customUserInfo.getUserId().toString();
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
}
