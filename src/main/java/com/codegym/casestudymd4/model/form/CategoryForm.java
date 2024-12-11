package com.codegym.casestudymd4.model.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryForm {
    private Long id;
    private String name;
    private MultipartFile image;
    private String oldImage;

    public CategoryForm(){

    }
}
