package com.codegym.casestudymd4.model.form;

import com.codegym.casestudymd4.model.BrandCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Trường tên sản phẩm không được để trống!")
    private String name;
    @NotNull(message = "Truờng giá sản phẩm không được để trống!")
    private BigDecimal price;
    @NotNull(message = "Truờng số lượng sản phẩm không được để trống!")
    private Long quantity;
    private MultipartFile image;
    private String oldImage;
    private String description;
    @ManyToOne
    @JoinColumn(name = "brand_category_id", nullable = false)
    private BrandCategory brandCategory;

    public ProductForm(){

    }
}
