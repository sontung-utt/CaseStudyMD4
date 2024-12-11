package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.BrandCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandCategoryRepository extends JpaRepository<BrandCategory, Long> {
    @Query("SELECT COUNT(b) FROM BrandCategory b WHERE b.brand.id = :idBrand AND b.category.id = :idCategory")
    Long countByBrandAndCategory(@Param("idBrand") Long idBrand, @Param("idCategory") Long idCategory);
}
