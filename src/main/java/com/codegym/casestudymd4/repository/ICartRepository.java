package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT a.id FROM Cart a where a.customer.id = :customerId")
    Long getIdByIdCustomer(@Param("customerId") Long customerId);
}
