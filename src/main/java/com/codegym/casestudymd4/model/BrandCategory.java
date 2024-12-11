package com.codegym.casestudymd4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brand_category", uniqueConstraints = {@UniqueConstraint(columnNames = {"brand_id", "category_id"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public BrandCategory(Brand brand, Category category) {
        this.brand = brand;
        this.category = category;
    }
}
