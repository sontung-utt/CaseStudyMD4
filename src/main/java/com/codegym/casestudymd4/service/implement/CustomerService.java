package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Customer;
import com.codegym.casestudymd4.repository.ICustomerRepository;
import com.codegym.casestudymd4.service.ICustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {
    private final ICustomerRepository iCustomerRepository;
    public CustomerService(ICustomerRepository iCustomerRepository){
        this.iCustomerRepository = iCustomerRepository;
    }
    @Override
    public Iterable<Customer> findAll() {
        return iCustomerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return iCustomerRepository.findById(id);
    }

    @Override
    public void save(Customer customer) {
        iCustomerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        iCustomerRepository.deleteById(id);
    }

    public boolean usedCustomerAccount(Long idUser){
        Long result = iCustomerRepository.usedAccount(idUser);
        return result != null && result > 0;
    }

    public boolean existEmail(String email){
        Long result = iCustomerRepository.existEmail(email);
        return result != null && result > 0;
    }

    public boolean existPhone(String phone){
        Long result = iCustomerRepository.existPhone(phone);
        return result != null && result > 0;
    }

    public Long getIdByUserId(Long userId){
        return iCustomerRepository.getIdByUserId(userId);
    }
}
