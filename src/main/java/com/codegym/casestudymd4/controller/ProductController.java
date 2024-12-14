package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.BrandCategory;
import com.codegym.casestudymd4.model.Product;
import com.codegym.casestudymd4.model.form.ProductForm;
import com.codegym.casestudymd4.service.IBrandCategoryService;
import com.codegym.casestudymd4.service.IProductService;
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
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Value("${file-upload}")
    private String uploadFile;

    private final IProductService iProductService;
    private final IBrandCategoryService iBrandCategoryService;
    @Autowired
    public ProductController(IProductService iProductService, IBrandCategoryService iBrandCategoryService){
        this.iProductService = iProductService;
        this.iBrandCategoryService = iBrandCategoryService;
    }
    @GetMapping("/list")
    public ModelAndView getProductList(){
        Iterable<Product> productList = iProductService.findAll();
        ModelAndView modelAndView = new ModelAndView("product/list");
        if (!productList.iterator().hasNext()){
            modelAndView.addObject("errorMessage", "Chưa có sản phẩm nào trong hệ thống.");
            modelAndView.addObject("productList", productList);
            return modelAndView;
        }
        modelAndView.addObject("productList", productList);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(){
        Iterable<BrandCategory> list = iBrandCategoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("product/add");
        modelAndView.addObject("productForm", new ProductForm());
        modelAndView.addObject("list", list);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addProduct(@Validated @ModelAttribute(name = "productForm") ProductForm productForm,
                             BindingResult bindingResult,
                             @RequestParam Long idBrandCategory,
                             Model model){
        Iterable<BrandCategory> list = iBrandCategoryService.findAll();
        if (bindingResult.hasErrors()){
            model.addAttribute("list", list);
            return "product/add";
        }
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(),new File(uploadFile + fileName));
        } catch (IOException e) {
           e.printStackTrace();
        }
        Optional<BrandCategory> brandCategory = iBrandCategoryService.findById(idBrandCategory);
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), productForm.getQuantity(), fileName, productForm.getWarranty(), productForm.getDescription(), brandCategory.get());
        iProductService.save(product);
        return "redirect:/products/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<Product> productOptional = iProductService.findById(id);
        Iterable<Product> productList = iProductService.findAll();
        Iterable<BrandCategory> list = iBrandCategoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("product/edit");
        if (!productOptional.isPresent()){
            modelAndView = new ModelAndView("product/list");
            modelAndView.addObject("message", "Không tìm thấy mã sản phẩm "+ id + "!");
            modelAndView.addObject("productList", productList);
            return modelAndView;
        }
        Product product = productOptional.get();
        ProductForm productForm = new ProductForm();
        productForm.setId(product.getId());
        productForm.setName(product.getName());
        productForm.setPrice(product.getPrice());
        productForm.setQuantity(product.getQuantity());
        productForm.setOldImage(product.getImage());
        productForm.setWarranty(product.getWarranty());
        productForm.setDescription(product.getDescription());
        productForm.setBrandCategory(product.getBrandCategory());

        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("list", list);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editProduct(@RequestParam Long id,
                              @Validated @ModelAttribute ProductForm productForm,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long idBrandCategory,
                              RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Các trường Tên sản phẩm - Giá sản phẩm - Số lượng - Thời gian bảo hành KHÔNG được để trống!");
            return "redirect:/products/edit?id=" + id;
        }
        MultipartFile multipartFile = productForm.getImage();
        String fileName = productForm.getOldImage();
        if (!multipartFile.isEmpty()){
            try {
                File oldFile = new File(uploadFile + productForm.getOldImage());
                if (oldFile.exists()){
                    oldFile.delete();
                }
                fileName = multipartFile.getOriginalFilename();
                FileCopyUtils.copy(productForm.getImage().getBytes(),new File(uploadFile + fileName));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Optional<Product> optionalProduct = iProductService.findById(id);
        Optional<BrandCategory> optionalBrandCategory = iBrandCategoryService.findById(idBrandCategory);
        BrandCategory brandCategory = optionalBrandCategory.get();
        if (!optionalProduct.isPresent()){
            redirectAttributes.addFlashAttribute("message", "Mã sản phẩm không tồn tại!");
            return "redirect:/products/list";
        }
        Product existProduct = optionalProduct.get();
        Product product = new Product(existProduct.getId(),productForm.getName(),productForm.getPrice(),productForm.getQuantity(),fileName,productForm.getWarranty(),productForm.getDescription(),brandCategory);
        iProductService.save(product);
        return "redirect:/products/list";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id){
        iProductService.delete(id);
        return "redirect:/products/list";
    }
}
