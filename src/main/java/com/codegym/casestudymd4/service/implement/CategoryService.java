package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Category;
import com.codegym.casestudymd4.repository.ICategoryRepository;
import com.codegym.casestudymd4.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    private final ICategoryRepository iCategoryRepository;
    @Autowired
    public CategoryService(ICategoryRepository iCategoryRepository){
        this.iCategoryRepository = iCategoryRepository;
    }

    @Override
    public Iterable<Category> findAll() {
        return iCategoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return iCategoryRepository.findById(id);
    }

    @Override
    public void save(Category category) {
        iCategoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        iCategoryRepository.deleteById(id);
    }

    public boolean existCategoryName(String name){
        Long result = iCategoryRepository.existCategoryName(name);
        return result != null && result > 0;
    }

    public List<Category> getCategoriesByBrand(Long brandId){
        return iCategoryRepository.findByBrandId(brandId);
    }
}
