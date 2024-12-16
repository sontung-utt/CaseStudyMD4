package com.codegym.casestudymd4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_cart", nullable = false)
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;
    private Long quantity;
    private BigDecimal price;
    private LocalDateTime created_at;
    private String formattedCreatedAt;

    @PrePersist
    public void prePersist(){
        created_at = LocalDateTime.now();
    }
}
