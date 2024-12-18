package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    @Query("select a.total from Order a where a.id = :orderId")
    BigDecimal getTotalByOrderId(@Param("orderId") Long orderId);

}
