package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c JOIN BrandCategory bc ON c.id = bc.category.id WHERE bc.brand.id = :brand_id")
    List<Category> findByBrandId(@Param("brand_id") Long brand_id);

    @Query("SELECT COUNT(c) FROM Category c where c.name = :name")
    Long existCategoryName (@Param("name") String name);
}
