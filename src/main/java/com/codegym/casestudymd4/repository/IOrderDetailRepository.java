package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("select a from OrderDetail a where a.order.id = :orderId")
    List<OrderDetail> getOrderDetailByOrderId(@Param("orderId") Long orderId);
}
