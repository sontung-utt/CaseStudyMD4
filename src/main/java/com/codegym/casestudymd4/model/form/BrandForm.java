package com.codegym.casestudymd4.model.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BrandForm {

    private Long id;
    private String name;
    private MultipartFile image;
    private String oldImage;

    public BrandForm(){

    }
}
