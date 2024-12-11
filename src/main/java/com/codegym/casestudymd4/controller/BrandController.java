package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Brand;
import com.codegym.casestudymd4.model.Category;
import com.codegym.casestudymd4.model.form.BrandForm;
import com.codegym.casestudymd4.service.IBrandService;
import com.codegym.casestudymd4.service.implement.BrandService;
import com.codegym.casestudymd4.service.implement.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/brands")
public class BrandController {
    @Value("${file-upload}")
    private String uploadFile;

    private final IBrandService iBrandService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    @Autowired
    public BrandController(IBrandService iBrandService, CategoryService categoryService, BrandService brandService){
        this.iBrandService = iBrandService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping("/list")
    public ModelAndView getBrandList(@RequestParam(value = "idBrand", required = false) Long idBrand){
        Iterable<Brand> brandList = iBrandService.findAll();
        ModelAndView modelAndView = new ModelAndView("brand/list");
        if (!brandList.iterator().hasNext()) {
            modelAndView.addObject("errorMessage", "Chưa có thương hiệu nào trong hệ thống.");
            modelAndView.addObject("brandList", brandList);
            modelAndView.addObject("categories", List.of());
            return modelAndView;
        }
        Long selectBrandId = (idBrand != null) ? idBrand : brandList.iterator().next().getId();
        List<Category> categories = categoryService.getCategoriesByBrand(selectBrandId);
        modelAndView.addObject("brandList",brandList);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(){
        ModelAndView modelAndView = new ModelAndView("brand/add");
        modelAndView.addObject("brandForm",new BrandForm());
        return modelAndView;
    }

    @PostMapping("/add")
    public String addBrand(@ModelAttribute("brandForm") BrandForm brandForm, @RequestParam("name") String name, Model model){
        MultipartFile multipartFile = brandForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try{
            FileCopyUtils.copy(brandForm.getImage().getBytes(),new File(uploadFile + fileName));
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(brandService.existBrandName(name)){
            model.addAttribute("errorMessage", "Thương hiệu đã tồn tại!");
            return "brand/add";
        }
        Brand brand = new Brand(brandForm.getId(), brandForm.getName(), fileName);
        iBrandService.save(brand);
        return "redirect:/brands/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<Brand> optionalBrand = iBrandService.findById(id);
        ModelAndView modelAndView = new ModelAndView("brand/edit");
        if (!optionalBrand.isPresent()) {
            throw new IllegalArgumentException("Invalid brand Id:" + id);
        }
        Brand brand = optionalBrand.get();
        BrandForm brandForm = new BrandForm();
        brandForm.setId(brand.getId());
        brandForm.setName(brand.getName());
        brandForm.setOldImage(brand.getImage());
        modelAndView.addObject("brandForm",brandForm);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editBrand(@RequestParam Long id, @ModelAttribute BrandForm brandForm, @RequestParam String name, RedirectAttributes redirectAttributes){
        MultipartFile multipartFile = brandForm.getImage();
        String fileName = brandForm.getOldImage();

        if (!multipartFile.isEmpty()) {
            try {
                File oldFile = new File(uploadFile + brandForm.getOldImage());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
                fileName = multipartFile.getOriginalFilename();
                FileCopyUtils.copy(brandForm.getImage().getBytes(), new File(uploadFile + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Optional<Brand> brandOptional = iBrandService.findById(id);
        if (!brandOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid brand Id:" + id);
        }

        Brand existBrand = brandOptional.get();

        if (!existBrand.getName().equals(name)) {
            if (brandService.existBrandName(name)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Thương hiệu đã tồn tại!");
                return "redirect:/brands/edit?id=" + id;
            }
        }

        Brand updatedBrand = new Brand(existBrand.getId(), name, fileName);
        iBrandService.save(updatedBrand);
        return "redirect:/brands/list";
    }

    @GetMapping("/delete")
    public String deleteBrand(@RequestParam("id") Long id){
        iBrandService.delete(id);
        return "redirect:/brands/list";
    }
}
