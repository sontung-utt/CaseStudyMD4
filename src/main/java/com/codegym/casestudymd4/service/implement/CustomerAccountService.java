package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.repository.ICustomerAccountRepository;
import com.codegym.casestudymd4.service.ICustomerAccountService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomerAccountService implements ICustomerAccountService {
    private final ICustomerAccountRepository iCustomerAccountRepository;
    public CustomerAccountService(ICustomerAccountRepository iCustomerAccountRepository){
        this.iCustomerAccountRepository = iCustomerAccountRepository;
    }
    @Override
    public Iterable<CustomerAccount> findAll() {
        return iCustomerAccountRepository.findAll();
    }

    @Override
    public Optional<CustomerAccount> findById(Long id) {
        return iCustomerAccountRepository.findById(id);
    }

    @Override
    public void save(CustomerAccount customerAccount) {
        iCustomerAccountRepository.save(customerAccount);
    }

    @Override
    public void delete(Long id) {
        iCustomerAccountRepository.deleteById(id);
    }

    public boolean existCustomerAccount(String username){
        Long result = iCustomerAccountRepository.existUsername(username);
        return result != null && result > 0;
    }

    public boolean existCustomerAccount(Long idUser){
        return iCustomerAccountRepository.existsById(idUser);
    }

    public Long getIdByUsername(String username){
        return iCustomerAccountRepository.findIdByUsername(username);
    }

    public CustomerAccount findByUsername (String username){
        return iCustomerAccountRepository.findByUsername(username);
    }

    public boolean checkCorrectPassword(String password, String rePassword){
        return password.equals(rePassword);
    }

    public boolean checkUser(String username, String password){
        Long result = iCustomerAccountRepository.checkUser(username, password);
        return result != null && result > 0;
    }

    public String getUsernameById(Long id){
        return iCustomerAccountRepository.getUsernameById(id);
    }

}
