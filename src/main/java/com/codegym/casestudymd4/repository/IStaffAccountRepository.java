package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Staff;
import com.codegym.casestudymd4.model.StaffAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStaffAccountRepository extends JpaRepository<StaffAccount,Long> {
    @Query("select s from StaffAccount s where s.role.id = :role")
    List<StaffAccount> findByRoleId(@Param("role") Long role);

    @Query("SELECT COUNT(b) FROM StaffAccount b where b.username = :username")
    Long existUsername (@Param("username") String username);
}
