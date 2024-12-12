package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Brand;
import com.codegym.casestudymd4.model.Category;
import com.codegym.casestudymd4.model.form.CategoryForm;
import com.codegym.casestudymd4.service.ICategoryService;
import com.codegym.casestudymd4.service.implement.BrandService;
import com.codegym.casestudymd4.service.implement.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Value("${file-upload}")
    private String uploadFile;

    private final ICategoryService iCategoryService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    @Autowired
    public CategoryController(ICategoryService iCategoryService, CategoryService categoryService, BrandService brandService){
        this.iCategoryService = iCategoryService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping("/list")
    public ModelAndView getCategoryList(@RequestParam(name = "idCategory", required = false) Long idCategory){
        Iterable<Category> categoryList = iCategoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("category/list");
        if (!categoryList.iterator().hasNext()) {
            modelAndView.addObject("errorMessage", "Chưa có loại sản phẩm nào trong hệ thống.");
            modelAndView.addObject("categoryList", categoryList);
            modelAndView.addObject("brands", List.of());
            return modelAndView;
        }
        Long selectCategoryId = (idCategory != null) ? idCategory : categoryList.iterator().next().getId();
        List<Brand> brands = brandService.getBrandsByCategory(selectCategoryId);
        if (brands.isEmpty()){
            modelAndView.addObject("message", "Không có thương hiệu nào!");
            modelAndView.addObject("categoryList", categoryList);
            modelAndView.addObject("brands", brands);
            modelAndView.addObject("selectCategoryId", selectCategoryId);
            return modelAndView;
        }
        modelAndView.addObject("categoryList", categoryList);
        modelAndView.addObject("brands", brands);
        modelAndView.addObject("selectCategoryId", selectCategoryId);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(){
        ModelAndView modelAndView = new ModelAndView("category/add");
        modelAndView.addObject("categoryForm", new CategoryForm());
        return modelAndView;
    }

    @PostMapping("/add")
    public String addCategory(@Validated @ModelAttribute CategoryForm categoryForm, BindingResult bindingResult, @RequestParam String name, Model model){
        if (bindingResult.hasErrors()) {
            return "category/add";
        }
        MultipartFile multipartFile = categoryForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(categoryForm.getImage().getBytes(),new File(uploadFile + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (categoryService.existCategoryName(name)){
            model.addAttribute("errorMessage", "Loại sản phẩm đã tồn tại!");
            return "category/add";
        }
        Category category = new Category(categoryForm.getId(), categoryForm.getName(), fileName);
        iCategoryService.save(category);
        return "redirect:/categories/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<Category> optionalCategory = iCategoryService.findById(id);
        Iterable<Category> categoryList = iCategoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("category/edit");
        if (!optionalCategory.isPresent()){
            modelAndView = new ModelAndView("category/list");
            modelAndView.addObject("message", "Không tìm thấy mã loại sản phẩm "+ id + "!");
            modelAndView.addObject("categoryList", categoryList);
            return modelAndView;
        }
        Category category = optionalCategory.get();
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setId(category.getId());
        categoryForm.setName(category.getName());
        categoryForm.setOldImage(category.getImage());
        modelAndView.addObject("categoryForm", categoryForm);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editCategory(@RequestParam Long id, @Validated @ModelAttribute CategoryForm categoryForm, BindingResult bindingResult, @RequestParam String name, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "redirect:/categories/edit?id=" + id;
        }
        MultipartFile multipartFile = categoryForm.getImage();
        String fileName = categoryForm.getOldImage();
        if (!multipartFile.isEmpty()){
            try {
                File oldFile = new File(uploadFile + categoryForm.getOldImage());
                if (oldFile.exists()){
                    oldFile.delete();
                }
                fileName = multipartFile.getOriginalFilename();
                FileCopyUtils.copy(categoryForm.getImage().getBytes(),new File(uploadFile + fileName));
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        Optional<Category> categoryOptional = iCategoryService.findById(id);
        if (!categoryOptional.isPresent()){
            redirectAttributes.addFlashAttribute("message", "Mã loại sản phẩm không tồn tại!");
            return "redirect:/categories/list";
        }

        Category existCategory = categoryOptional.get();

        if (!existCategory.getName().equals(name)) {
            if (categoryService.existCategoryName(name)){
                redirectAttributes.addFlashAttribute("errorMessage", "Loại sản phẩm đã tồn tại!");
                return "redirect:/categories/edit?id=" + id;
            }
        }

        Category updateCategory = new Category(existCategory.getId(), name, fileName);
        iCategoryService.save(updateCategory);
        return "redirect:/categories/list";
    }

    @GetMapping("/delete")
    public String deleteCategory (@RequestParam Long id){
        iCategoryService.delete(id);
        return "redirect:/categories/list";
    }
}
