package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.service.ICustomerAccountService;
import com.codegym.casestudymd4.service.IStaffAccountService;
import com.codegym.casestudymd4.service.implement.CustomerAccountService;
import com.codegym.casestudymd4.service.implement.StaffAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final ICustomerAccountService iCustomerAccountService;
    private final CustomerAccountService customerAccountService;
    private final IStaffAccountService iStaffAccountService;
    private final StaffAccountService staffAccountService;
    @Autowired
    public LoginController(ICustomerAccountService iCustomerAccountService, CustomerAccountService customerAccountService,
                           IStaffAccountService iStaffAccountService, StaffAccountService staffAccountService){
        this.iCustomerAccountService = iCustomerAccountService;
        this.customerAccountService = customerAccountService;
        this.iStaffAccountService = iStaffAccountService;
        this.staffAccountService = staffAccountService;
    }
    @GetMapping()
    public ModelAndView showLoginForm(){
        ModelAndView modelAndView = new ModelAndView("user/login");
        modelAndView.addObject("user",new CustomerAccount());
        return modelAndView;
    }

    @PostMapping()
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
