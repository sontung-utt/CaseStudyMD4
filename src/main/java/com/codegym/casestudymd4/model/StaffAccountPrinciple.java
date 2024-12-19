package com.codegym.casestudymd4.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class StaffAccountPrinciple implements UserDetails {

    private final StaffAccount staffAccount;

    public StaffAccountPrinciple(StaffAccount staffAccount) {
        this.staffAccount = staffAccount;
    }

    public Long getId() {
        return staffAccount.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(staffAccount.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return staffAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return staffAccount.getUsername();
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
