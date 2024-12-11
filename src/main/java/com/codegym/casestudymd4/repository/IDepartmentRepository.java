package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT COUNT(b) FROM Department b where b.name = :name")
    Long existDepartmentName (@Param("name") String name);
}
