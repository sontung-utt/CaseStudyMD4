package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.model.StaffAccountPrinciple;
import com.codegym.casestudymd4.repository.IStaffAccountRepository;
import com.codegym.casestudymd4.service.IStaffAccountService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StaffAccountService implements IStaffAccountService, UserDetailsService {
    private final IStaffAccountRepository iStaffAccountRepository;

    public StaffAccountService(IStaffAccountRepository iStaffAccountRepository) {
        this.iStaffAccountRepository = iStaffAccountRepository;
    }

    @Override
    public Iterable<StaffAccount> findAll() {
        return iStaffAccountRepository.findAll();
    }

    @Override
    public Optional<StaffAccount> findById(Long id) {
        return iStaffAccountRepository.findById(id);
    }

    @Override
    public void save(StaffAccount staffAccount) {
        iStaffAccountRepository.save(staffAccount);
    }

    @Override
    public void delete(Long id) {
        iStaffAccountRepository.deleteById(id);
    }

    public List<StaffAccount> getStaffAccountsByRole(Long roleId) {
        return iStaffAccountRepository.findByRoleId(roleId);
    }

    public boolean existStaffAccount(String username) {
        Long result = iStaffAccountRepository.existUsername(username);
        return result != null && result > 0;
    }

    public boolean existStaffAccount(Long idUser) {
        return iStaffAccountRepository.existsById(idUser);
    }

    public Long getIdByUsername(String username) {
        return iStaffAccountRepository.findIdByUsername(username);
    }

    public StaffAccount findByUsername(String username) {
        return iStaffAccountRepository.findByUsername(username);
    }

    public boolean checkUser(String username, String password) {
        Long result = iStaffAccountRepository.checkUser(username, password);
        return result != null && result > 0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return StaffAccountPrinciple.build(iStaffAccountRepository.findByUsername(username));
    }

    public String getUsernameById(Long id) {
        return iStaffAccountRepository.getUsernameById(id);
    }
}
