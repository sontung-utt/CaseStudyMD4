package com.codegym.casestudymd4.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BrandForm {

    private Long id;
    @NotBlank(message = "Trường tên thương hiệu không được để trống!")
    private String name;
    private MultipartFile image;
    private String oldImage;

    public BrandForm(){

    }
}
