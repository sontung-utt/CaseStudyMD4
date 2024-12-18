package com.codegym.casestudymd4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal total;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "id_cart", nullable = false)
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;
    @Column(columnDefinition = "varchar(255) default 'Pending'")
    private String status;
    private String formattedTime;
    @PrePersist
    public void prePersist(){
        time = LocalDateTime.now();
    }

    public Order(BigDecimal total, Cart cart, Customer customer){
        this.total = total;
        this.cart = cart;
        this.customer = customer;
    }
}
