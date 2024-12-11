package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Brand;
import com.codegym.casestudymd4.model.BrandCategory;
import com.codegym.casestudymd4.model.Category;
import com.codegym.casestudymd4.service.IBrandCategoryService;
import com.codegym.casestudymd4.service.IBrandService;
import com.codegym.casestudymd4.service.ICategoryService;
import com.codegym.casestudymd4.service.implement.BrandCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/brand_category")
public class BrandCategoryController {
    private final IBrandCategoryService iBrandCategoryService;
    private final BrandCategoryService brandCategoryService;
    private final IBrandService iBrandService;
    private final ICategoryService iCategoryService;
    @Autowired
    public BrandCategoryController(IBrandCategoryService iBrandCategoryService, BrandCategoryService brandCategoryService, IBrandService iBrandService, ICategoryService iCategoryService){
        this.iBrandCategoryService = iBrandCategoryService;
        this.brandCategoryService = brandCategoryService;
        this.iBrandService = iBrandService;
        this.iCategoryService = iCategoryService;
    }

    @GetMapping("/list")
    public ModelAndView getList() {
        Iterable<BrandCategory> list = iBrandCategoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("brand_category/list");
        if (!list.iterator().hasNext()){
            modelAndView.addObject("errorMessage","Hiện tại danh sách đang rỗng!");
            modelAndView.addObject("list", list);
            return modelAndView;
        }
        modelAndView.addObject("list", list);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd() {
        Iterable<Brand> brandList = iBrandService.findAll();
        Iterable<Category> categoryList = iCategoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("brand_category/add");
        modelAndView.addObject("brandList", brandList);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addBrandCategory(@RequestParam Long idBrand, @RequestParam Long idCategory, Model model){
        if (brandCategoryService.existBrandCategory(idBrand, idCategory)) {
            model.addAttribute("errorMessage", "Mã thương hiệu và mã loại sản phẩm đã tồn tại!");
            model.addAttribute("brandList", iBrandService.findAll());
            model.addAttribute("categoryList", iCategoryService.findAll());
            return "brand_category/add";
        }
        Optional<Brand> brandOptional = iBrandService.findById(idBrand);
        Optional<Category> categoryOptional = iCategoryService.findById(idCategory);
        Brand brand = brandOptional.get();
        Category category = categoryOptional.get();
        BrandCategory brandCategory = new BrandCategory(brand,category);
        iBrandCategoryService.save(brandCategory);
        return "redirect:/brand_category/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<BrandCategory> brandCategoryOptional = iBrandCategoryService.findById(id);
        Iterable<Brand> brandList = iBrandService.findAll();
        Iterable<Category> categoryList = iCategoryService.findAll();
        BrandCategory brandCategory = brandCategoryOptional.get();
        ModelAndView modelAndView = new ModelAndView("brand_category/edit");
        modelAndView.addObject("brandCategory",brandCategory);
        modelAndView.addObject("brandList", brandList);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editBrandCategory(@RequestParam Long id, @RequestParam Long idBrand, @RequestParam Long idCategory, Model model, RedirectAttributes redirectAttributes){
        Optional<BrandCategory> brandCategoryOptional = brandCategoryService.findById(id);
        BrandCategory existBrandCategory = brandCategoryOptional.get();
        if (existBrandCategory.getCategory().getId() != idCategory || existBrandCategory.getBrand().getId() != idBrand){
            if (brandCategoryService.existBrandCategory(idBrand,idCategory)){
                redirectAttributes.addFlashAttribute("errorMessage", "Mã thương hiệu và mã loại sản phẩm đã tồn tại!");
//                model.addAttribute("brandList", iBrandService.findAll());
//                model.addAttribute("categoryList", iCategoryService.findAll());
                return "redirect:/brand_category/edit?id=" + id;
            }
        }
        Optional<Brand> brandOptional = iBrandService.findById(idBrand);
        Optional<Category> categoryOptional = iCategoryService.findById(idCategory);
        Brand brand = brandOptional.get();
        Category category = categoryOptional.get();
        BrandCategory brandCategory = new BrandCategory(id, brand, category);
        iBrandCategoryService.save(brandCategory);
        return "redirect:/brand_category/list";
    }
}
