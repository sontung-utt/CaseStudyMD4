package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.repository.ICustomerAccountRepository;
import com.codegym.casestudymd4.repository.IStaffAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private ICustomerAccountRepository iCustomerAccountRepository;

    @Autowired
    private IStaffAccountRepository iStaffAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        StaffAccount staffAccount = iStaffAccountRepository.findByUsername(username);
        if (staffAccount != null){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(staffAccount.getRole().getName()));
            return new User(staffAccount.getUsername(), staffAccount.getPassword(), authorities);
        }
        throw new UsernameNotFoundException("Không tìm thấy người dùng!");
    }
}
