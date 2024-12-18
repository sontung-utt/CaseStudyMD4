package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Staff;
import com.codegym.casestudymd4.model.StaffAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IStaffAccountRepository extends JpaRepository<StaffAccount,Long> {
    @Query("select s from StaffAccount s where s.role.id = :roleId")
    List<StaffAccount> findByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT COUNT(b) FROM StaffAccount b where b.username = :username")
    Long existUsername (@Param("username") String username);

    @Query("SELECT COUNT(b) FROM StaffAccount b where b.id = :idUser")
    Long existAccount (@Param("idUser") Long idUser);

    @Query("select a.id from StaffAccount a where a.username = :username")
    Long findIdByUsername(String username);

    StaffAccount findByUsername(String username);

    @Query("SELECT COUNT(b) FROM StaffAccount b where b.username = :username and b.password = :password")
    Long checkUser (@Param("username") String username, @Param("password") String password);

    @Query("SELECT a.username FROM StaffAccount a where a.id = :id")
    String getUsernameById(@Param("id") Long id);
}
