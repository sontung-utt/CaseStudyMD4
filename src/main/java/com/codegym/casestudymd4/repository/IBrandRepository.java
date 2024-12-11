package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Brand;
import com.codegym.casestudymd4.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b JOIN BrandCategory bc ON b.id = bc.brand.id WHERE bc.category.id = :category_id")
    List<Brand> findByCategoryId(@Param("category_id") Long category_id);

    @Query("SELECT COUNT(b) FROM Brand b where b.name = :name")
    Long existBrandName (@Param("name") String name);
}
