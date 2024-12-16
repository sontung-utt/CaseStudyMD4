package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Brand;
import com.codegym.casestudymd4.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT COUNT(b) FROM Role b where b.name = :name")
    Long existRoleName (@Param("name") String name);

    Role findByName(String name);

    @Query("SELECT r.displayName FROM Role r WHERE r.name = :name")
    String findDisplayNameByName(@Param("name") String name);
}
