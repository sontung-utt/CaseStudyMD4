package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStaffRepository extends JpaRepository<Staff, Long> {
    @Query("select s from Staff s where s.department.id = :department_id")
    List<Staff> findByDepartmentId(@Param("department_id") Long department_id);
}
