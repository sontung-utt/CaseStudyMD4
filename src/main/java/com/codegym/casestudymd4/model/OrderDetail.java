package com.codegym.casestudymd4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;
    private Long quantity;
    private BigDecimal price;

    public OrderDetail(Order order, Product product, Long quantity, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
