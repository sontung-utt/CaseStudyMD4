package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.BrandCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandCategoryRepository extends JpaRepository<BrandCategory, Long> {
}
