package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Product;
import com.codegym.casestudymd4.repository.IProductRepository;
import com.codegym.casestudymd4.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final IProductRepository iProductRepository;
    @Autowired
    public ProductService(IProductRepository iProductRepository){
        this.iProductRepository = iProductRepository;
    }
    @Override
    public Iterable<Product> findAll() {
        return iProductRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return iProductRepository.findById(id);
    }

    @Override
    public void save(Product product) {
        iProductRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        iProductRepository.deleteById(id);
    }
}
