package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.service.ICustomerAccountService;
import com.codegym.casestudymd4.service.implement.CustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class CustomerAccountController {
    private final ICustomerAccountService iCustomerAccountService;
    private final CustomerAccountService customerAccountService;
    @Autowired
    public CustomerAccountController(ICustomerAccountService iCustomerAccountService, CustomerAccountService customerAccountService){
        this.iCustomerAccountService = iCustomerAccountService;
        this.customerAccountService = customerAccountService;
    }
    @GetMapping("/register")
    public ModelAndView showFormRegister(){
        ModelAndView modelAndView = new ModelAndView("user/register");
        modelAndView.addObject("user", new CustomerAccount());
        return modelAndView;
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute CustomerAccount customerAccount,
                                 BindingResult bindingResult,
                                 @RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("rePassword") String rePassword,
                                 Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("user", customerAccount);
            return "user/register";
        }
        if(customerAccountService.existCustomerAccount(username)){
            model.addAttribute("errorMessage", "Tài khoản đã tồn tại!");
            model.addAttribute("user", customerAccount);
            return "user/register";
        }
        if (!customerAccountService.checkCorrectPassword(password, rePassword)){
            model.addAttribute("errorMessage", "Mật khẩu và Nhập lại mật khẩu không trùng khớp!");
            model.addAttribute("user", customerAccount);
            return "user/register";
        }
        customerAccount.setUsername(username);
        customerAccount.setPassword(password);
        iCustomerAccountService.save(customerAccount);
        return "user/login";
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(){
        ModelAndView modelAndView = new ModelAndView("user/login");
        modelAndView.addObject("user",new CustomerAccount());
        return modelAndView;
    }

    @PostMapping("/login")
    public String loginCustomer(@ModelAttribute CustomerAccount customerAccount,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password,
                                Model model){
        model.addAttribute("user", customerAccount);
        if (customerAccountService.checkUser(username, password)){
            return "redirect:/home";
        } else {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "user/login";
        }
    }
}
