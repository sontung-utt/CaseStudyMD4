package com.codegym.casestudymd4.model.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryForm {
    private Long id;
    @NotBlank(message = "Trường tên loại sản phẩm không được để trống!")
    private String name;
    private MultipartFile image;
    private String oldImage;

    public CategoryForm(){

    }
}
