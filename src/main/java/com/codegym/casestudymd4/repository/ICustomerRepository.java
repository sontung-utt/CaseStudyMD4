package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT COUNT(a) FROM Customer a where a.customerAccount.id = :idUser")
    Long usedAccount(@Param("idUser") Long idUser);

    @Query("SELECT COUNT(a) FROM Customer a where a.email = :email")
    Long existEmail(@Param("email") String email);

    @Query("SELECT COUNT(a) FROM Customer a where a.phone = :phone")
    Long existPhone(@Param("phone") String phone);

    @Query("SELECT a.id FROM Customer a where a.customerAccount.id = :userId    ")
    Long getIdByUserId(@Param("userId") Long userId);

}
