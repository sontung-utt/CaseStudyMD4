package com.codegym.casestudymd4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Trường tên sản phẩm không được để trống!")
    private String name;
    @NotNull(message = "Truờng giá sản phẩm không được để trống!")
    private BigDecimal price;
    @NotNull(message = "Truờng số lượng sản phẩm không được để trống!")
    private Long quantity;
    private String image;
    @NotNull(message = "Truờng thời gian bảo hành sản phẩm không được để trống!")
    private Integer warranty;
    private String description;
    @ManyToOne
    @JoinColumn(name = "brand_category_id", nullable = false)
    private BrandCategory brandCategory;
}
