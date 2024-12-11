package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Brand;
import com.codegym.casestudymd4.repository.IBrandRepository;
import com.codegym.casestudymd4.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService implements IBrandService {

    private final IBrandRepository iBrandRepository;
    @Autowired
    public BrandService(IBrandRepository iBrandRepository){
        this.iBrandRepository = iBrandRepository;
    }

    @Override
    public Iterable<Brand> findAll() {
        return iBrandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return iBrandRepository.findById(id);
    }

    @Override
    public void save(Brand brand) {
        iBrandRepository.save(brand);
    }

    @Override
    public void delete(Long id) {
        iBrandRepository.deleteById(id);
    }

    public boolean existBrandName(String name){
        Long result = iBrandRepository.existBrandName(name);
        return result != null && result > 0;
    }

    public List<Brand> getBrandsByCategory(Long categoryId){
        return iBrandRepository.findByCategoryId(categoryId);
    }
}
