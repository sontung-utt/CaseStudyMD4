package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Product;
import com.codegym.casestudymd4.service.ICustomerAccountService;
import com.codegym.casestudymd4.service.ICustomerService;
import com.codegym.casestudymd4.service.IProductService;
import com.codegym.casestudymd4.service.implement.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class CustomerController {
    private final ICustomerService iCustomerService;
    private final ICustomerAccountService iCustomerAccountService;
    private final CustomerService customerService;
    private final IProductService iProductService;
    @Autowired
    public CustomerController(ICustomerService iCustomerService, ICustomerAccountService iCustomerAccountService, CustomerService customerService, IProductService iProductService){
        this.iCustomerService = iCustomerService;
        this.iCustomerAccountService = iCustomerAccountService;
        this.customerService = customerService;
        this.iProductService = iProductService;
    }

    @GetMapping
    public ModelAndView showHome(){
        ModelAndView modelAndView = new ModelAndView("customer/home");
        Iterable<Product> products = iProductService.findAll();
        modelAndView.addObject("products", products);
        return modelAndView;
    }
}
