package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.service.ICustomerAccountService;
import com.codegym.casestudymd4.service.IStaffAccountService;
import com.codegym.casestudymd4.service.implement.CustomerAccountService;
import com.codegym.casestudymd4.service.implement.StaffAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final ICustomerAccountService iCustomerAccountService;
    private final CustomerAccountService customerAccountService;
    @Autowired
    public LoginController(ICustomerAccountService iCustomerAccountService, CustomerAccountService customerAccountService){
        this.iCustomerAccountService = iCustomerAccountService;
        this.customerAccountService = customerAccountService;
    }
    @GetMapping()
    public ModelAndView showLoginForm(){
        ModelAndView modelAndView = new ModelAndView("user/login");
        modelAndView.addObject("user",new CustomerAccount());
        return modelAndView;
    }

    @PostMapping()
    public String loginCustomer(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request,
                                Model model){
        Long idLogin = customerAccountService.getIdByUsername(username);
        if (customerAccountService.checkUser(username, password)){
            HttpSession session = request.getSession();
            session.invalidate();
            session = request.getSession(true);
            session.setAttribute("idLogin", idLogin);
            session.setAttribute("customerUsername", username);
            return "redirect:/home";
        } else {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            model.addAttribute("user", new CustomerAccount());
            return "user/login";
        }
    }
}
