package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.BrandCategory;
import com.codegym.casestudymd4.repository.IBrandCategoryRepository;
import com.codegym.casestudymd4.service.IBrandCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandCategoryService implements IBrandCategoryService {
    private final IBrandCategoryRepository iBrandCategoryRepository;
    @Autowired
    public BrandCategoryService(IBrandCategoryRepository iBrandCategoryRepository){
        this.iBrandCategoryRepository = iBrandCategoryRepository;
    }

    @Override
    public Iterable<BrandCategory> findAll() {
        return iBrandCategoryRepository.findAll();
    }

    @Override
    public Optional<BrandCategory> findById(Long id) {
        return iBrandCategoryRepository.findById(id);
    }

    @Override
    public void save(BrandCategory brandCategory) {
        iBrandCategoryRepository.save(brandCategory);
    }

    @Override
    public void delete(Long id) {
        iBrandCategoryRepository.deleteById(id);
    }
}
