package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    @Query("SELECT COUNT(b) FROM CustomerAccount b where b.username = :username")
    Long existUsername (@Param("username") String username);

    @Query("SELECT COUNT(b) FROM CustomerAccount b where b.id = :idUser")
    Long existAccount (@Param("idUser") Long idUser);

    CustomerAccount findByUsername(String username);

    @Query("select a.id from CustomerAccount a where a.username = :username")
    Long findIdByUsername(String username);

    @Query("SELECT COUNT(b) FROM CustomerAccount b where b.username = :username and b.password = :password")
    Long checkUser (@Param("username") String username, @Param("password") String password);
}
