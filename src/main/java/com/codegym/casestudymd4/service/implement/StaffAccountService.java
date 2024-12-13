package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.repository.IStaffAccountRepository;
import com.codegym.casestudymd4.service.IStaffAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffAccountService implements IStaffAccountService {
    private final IStaffAccountRepository iStaffAccountRepository;
    public StaffAccountService(IStaffAccountRepository iStaffAccountRepository){
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

    public List<StaffAccount> getStaffAccountsByRole(Long roleId){
        return iStaffAccountRepository.findByRoleId(roleId);
    }

    public boolean existStaffAccount(String username){
        Long result = iStaffAccountRepository.existUsername(username);
        return result != null && result > 0;
    }
}
