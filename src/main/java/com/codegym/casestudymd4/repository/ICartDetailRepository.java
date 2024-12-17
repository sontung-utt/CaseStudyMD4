package com.codegym.casestudymd4.repository;

import com.codegym.casestudymd4.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartDetailRepository extends JpaRepository<CartDetail, Long> {
    @Query("select count(a.product.id) from CartDetail a join Cart b on a.cart.id = b.id where a.cart.id = :cartId and b.customer.id = :customerId")
    Long getNumProduct(@Param("cartId") Long cartId, @Param("customerId") Long customerId);

    @Query("select a from CartDetail a where a.cart.id = :idCart")
    List<CartDetail> getListByCartId (@Param("idCart") Long idCart);

    @Query("select count(a) from CartDetail a where a.cart.id = :idCart and a.product.id = :idProduct")
    Long existProductInCart (@Param("idCart") Long idCart, @Param("idProduct") Long idProduct);

    @Query("select a from CartDetail a where a.cart.id = :idCart and a.product.id = :idProduct")
    CartDetail findByIdProduct(@Param("idCart") Long idCart, @Param("idProduct") Long idProduct);
}
