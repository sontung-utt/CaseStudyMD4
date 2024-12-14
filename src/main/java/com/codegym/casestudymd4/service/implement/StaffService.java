package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Staff;
import com.codegym.casestudymd4.repository.IStaffRepository;
import com.codegym.casestudymd4.service.IStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService implements IStaffService {
    private final IStaffRepository iStaffRepository;
    @Autowired
    public StaffService(IStaffRepository iStaffRepository){
        this.iStaffRepository = iStaffRepository;
    }
    @Override
    public Iterable<Staff> findAll() {
        return iStaffRepository.findAll();
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return iStaffRepository.findById(id);
    }

    @Override
    public void save(Staff staff) {
        iStaffRepository.save(staff);
    }

    @Override
    public void delete(Long id) {
        iStaffRepository.deleteById(id);
    }

    public List<Staff> getStaffsByDepartment(Long departmentId) {
        return iStaffRepository.findByDepartmentId(departmentId);
    }

    public boolean usedStaffAccount(Long idUser){
        Long result = iStaffRepository.usedAccount(idUser);
        return result != null && result > 0;
    }

    public boolean existEmail(String email){
        Long result = iStaffRepository.existEmail(email);
        return result != null && result > 0;
    }

    public boolean existPhone(String phone){
        Long result = iStaffRepository.existPhone(phone);
        return result != null && result > 0;
    }
}
