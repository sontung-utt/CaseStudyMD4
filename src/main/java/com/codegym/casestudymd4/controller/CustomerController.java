package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Cart;
import com.codegym.casestudymd4.model.Customer;
import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.model.Product;
import com.codegym.casestudymd4.service.*;
import com.codegym.casestudymd4.service.implement.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/home")
public class CustomerController {
    private final ICustomerService iCustomerService;
    private final ICustomerAccountService iCustomerAccountService;
    private final CustomerAccountService customerAccountService;
    private final CustomerService customerService;
    private final IProductService iProductService;
    //private final CustomUserDetailService customUserDetailService;
    private final CartService cartService;
    private final ICartService iCartService;
    private final CartDetailService cartDetailService;
    @Autowired
    public CustomerController(ICustomerService iCustomerService,
                              ICustomerAccountService iCustomerAccountService,
                              CustomerService customerService,
                              IProductService iProductService,
                              CustomerAccountService customerAccountService,
                              //CustomUserDetailService customUserDetailService,
                              ICartService iCartService,
                              CartService cartService,
                              CartDetailService cartDetailService){
        this.iCustomerService = iCustomerService;
        this.iCustomerAccountService = iCustomerAccountService;
        this.customerService = customerService;
        this.iProductService = iProductService;
        //this.customUserDetailService = customUserDetailService;
        this.iCartService = iCartService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.customerAccountService = customerAccountService;
    }

    @GetMapping
    public String showHome(HttpSession session, Model model){
        Long idLogin = (Long) session.getAttribute("idLogin");
        if (idLogin != null && idLogin != -1) {
            Iterable<Product> products = iProductService.findAll();
            Long idCustomer = customerService.getIdByUserId(idLogin);
            String customerUsername = customerAccountService.getUsernameById(idLogin);
            if (idCustomer == null){
                model.addAttribute("products", products);
                model.addAttribute("customerUsername", customerUsername);
                return "customer/home";
            }
            Long idCart = cartService.getIdByIdCustomer(idCustomer);
            Long numProduct = cartDetailService.getNumProduct(idCart, idCustomer);
            model.addAttribute("products", products);
            model.addAttribute("numProduct", numProduct);
            model.addAttribute("customerUsername", customerUsername);

            return "customer/home";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/list")
    private ModelAndView showCustomerList(){
        ModelAndView modelAndView = new ModelAndView("customer/list");
        Iterable<Customer> customers = iCustomerService.findAll();
        if (!customers.iterator().hasNext()){
            modelAndView.addObject("errorMessage","Chưa có khách hàng nào trong hệ thống");
            modelAndView.addObject("customers", customers);
            return modelAndView;
        }
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

    @GetMapping("/add")
    private String showFormAddInfo(HttpSession session, Model model) {
        Long idLogin = (Long) session.getAttribute("idLogin");
        if (idLogin != null && idLogin != -1) {
            Optional<CustomerAccount> customerAccountOptional = iCustomerAccountService.findById(idLogin);
            CustomerAccount customerAccount = customerAccountOptional.get();
            model.addAttribute("customerAccount", customerAccount);
            String username = customerAccountService.getUsernameById(idLogin);
            model.addAttribute("username", username);
            Long idCustomer = customerService.getIdByUserId(idLogin);
            if (idCustomer == null) {
                model.addAttribute("customer", new Customer());
                return "customer/add";
            } else {
                Optional<Customer> customerOptional = customerService.findById(idCustomer);
                Customer customer = customerOptional.get();
                model.addAttribute("customer", customer);
                return "customer/edit";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/add")
    public String addInfoCustomer (@Validated @ModelAttribute(name = "customer") Customer customer,
                                   BindingResult bindingResult,
                                   @RequestParam String phone,
                                   @RequestParam String email,
                                   HttpSession session,
                                   Model model){
        Long idLogin = (Long) session.getAttribute("idLogin");
        String username = customerAccountService.getUsernameById(idLogin);
        if (bindingResult.hasErrors()){
            model.addAttribute("username", username);
            return "customer/add";
        }
        model.addAttribute("username", username);
        Optional<CustomerAccount> customerAccountOptional = iCustomerAccountService.findById(idLogin);
        CustomerAccount customerAccount = customerAccountOptional.get();

        if(customerService.existPhone(phone)){
            model.addAttribute("errorMessage", "Số điện thoại đã được sử dụng!");
            return "customer/add";
        }
        if(customerService.existEmail(email)){
            model.addAttribute("errorMessage", "Email đã được sử dụng!");
            return "customer/add";
        }
        Customer newCustomer = new Customer(customer.getId(), customer.getName(), customer.getGender(), customer.getBirth(), customer.getAddress(), email, phone, customer.getRanking(), customerAccount);
        iCustomerService.save(newCustomer);
        Cart cart = new Cart(newCustomer);
        iCartService.save(cart);
        return "redirect:/home";
    }

    @PostMapping("/edit")
    public String editInfoCustomer(@Validated @ModelAttribute(name = "customer") Customer customer,
                                   BindingResult bindingResult,
                                   @RequestParam String phone,
                                   @RequestParam String email,
                                   HttpSession session,
                                   Model model) {
        Long idLogin = (Long) session.getAttribute("idLogin");
        String username = customerAccountService.getUsernameById(idLogin);
        if (bindingResult.hasErrors()) {
            model.addAttribute("username", username);
            return "customer/edit";
        }
        Long idCustomer = customerService.getIdByUserId(idLogin);
        Optional<CustomerAccount> customerAccountOptional = iCustomerAccountService.findById(idLogin);
        CustomerAccount customerAccount = customerAccountOptional.get();
        Optional<Customer> customerOptional = iCustomerService.findById(idCustomer);
        Customer existCustomer = customerOptional.get();
        model.addAttribute("username", username);
        if (!existCustomer.getPhone().equals(phone)){
            if(customerService.existPhone(phone)){
                model.addAttribute("errorMessage", "Số điện thoại đã được sử dụng!");
                return "customer/edit";
            }
        }
        if (!existCustomer.getEmail().equals(email)){
            if(customerService.existEmail(email)){
                model.addAttribute("errorMessage", "Email đã được sử dụng!");
                return "customer/edit";
            }
        }
        Customer updateCustomer = new Customer(existCustomer.getId(), customer.getName(), customer.getGender(), customer.getBirth(), customer.getAddress(), email, phone, customer.getRanking(), customerAccount);
        iCustomerService.save(updateCustomer);
        return "redirect:/home";
    }
}
