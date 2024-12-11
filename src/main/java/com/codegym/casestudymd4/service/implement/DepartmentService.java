package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Department;
import com.codegym.casestudymd4.repository.IDepartmentRepository;
import com.codegym.casestudymd4.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentService implements IDepartmentService {
    private final IDepartmentRepository iDepartmentRepository;
    @Autowired
    public DepartmentService(IDepartmentRepository iDepartmentRepository){
        this.iDepartmentRepository = iDepartmentRepository;
    }
    @Override
    public Iterable<Department> findAll() {
        return iDepartmentRepository.findAll();
    }

    @Override
    public Optional<Department> findById(Long id) {
        return iDepartmentRepository.findById(id);
    }

    @Override
    public void save(Department department) {
        iDepartmentRepository.save(department);
    }

    @Override
    public void delete(Long id) {
        iDepartmentRepository.deleteById(id);
    }

    public boolean existBrandDepartment(String name){
        Long result = iDepartmentRepository.existDepartmentName(name);
        return result != null && result > 0;
    }
}
