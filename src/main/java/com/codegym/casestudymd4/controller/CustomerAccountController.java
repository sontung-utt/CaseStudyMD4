package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.service.ICustomerAccountService;
import com.codegym.casestudymd4.service.implement.CustomerAccountService;
import com.codegym.casestudymd4.service.implement.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @GetMapping("/list")
    public ModelAndView getListAccount(){
        ModelAndView modelAndView = new ModelAndView("customer/list_account");
        Iterable<CustomerAccount> list = iCustomerAccountService.findAll();
        if (!list.iterator().hasNext()){
            modelAndView.addObject("errorMessage", "Chưa có tài khoản khách hàng nào trong hệ thống!");
            modelAndView.addObject("list", list);
            return modelAndView;
        }
        for (CustomerAccount account : list){
            account.setCreated_at(LocalDateTime.parse(formatLocalDateTime(account.getCreated_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            account.setModified_at(LocalDateTime.parse(formatLocalDateTime(account.getModified_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            account.setFormattedCreatedAt(formatLocalDateTime(account.getCreated_at()));
            account.setFormattedModifiedAt(formatLocalDateTime(account.getModified_at()));
        }
        modelAndView.addObject("list", list);
        return modelAndView;
    }

    @GetMapping("/account")
    public ModelAndView showInfoAccount(HttpSession session){
        ModelAndView modelAndView = new ModelAndView("customer/account");
        Long idLogin = (Long) session.getAttribute("idLogin");
        Optional<CustomerAccount> customerAccountOptional = iCustomerAccountService.findById(idLogin);
        CustomerAccount customerAccount = customerAccountOptional.get();
        customerAccount.setCreated_at(LocalDateTime.parse(formatLocalDateTime(customerAccount.getCreated_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        customerAccount.setModified_at(LocalDateTime.parse(formatLocalDateTime(customerAccount.getModified_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        customerAccount.setFormattedCreatedAt(formatLocalDateTime(customerAccount.getCreated_at()));
        customerAccount.setFormattedModifiedAt(formatLocalDateTime(customerAccount.getModified_at()));
        modelAndView.addObject("customerAccount", customerAccount);
        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(HttpSession session){
        Long idLogin = (Long) session.getAttribute("idLogin");
        Optional<CustomerAccount> customerAccountOptional = iCustomerAccountService.findById(idLogin);
        CustomerAccount customerAccount = customerAccountOptional.get();
        ModelAndView modelAndView = new ModelAndView("customer/edit_account");
        modelAndView.addObject("customerAccount", customerAccount);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editAccount(@Validated @ModelAttribute CustomerAccount customerAccount,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model){
        Long id = (Long) session.getAttribute("idLogin");
        if(bindingResult.hasErrors()){
            model.addAttribute("customerAccount", customerAccount);
            return "customer/edit_account";
        }
        Optional<CustomerAccount> customerAccountOptional = iCustomerAccountService.findById(id);
        CustomerAccount existAccount = customerAccountOptional.get();
        String username = customerAccount.getUsername();
        if (!existAccount.getUsername().equals(username)){
            if (customerAccountService.existCustomerAccount(username)){
                model.addAttribute("errorMessage", "Tên tài khoản đã tồn tại!");
                model.addAttribute("customerAccount", customerAccount);
                return "customer/edit_account";
            }
        }
        String password = customerAccount.getPassword();
        customerAccount.setCreated_at(existAccount.getCreated_at());
        customerAccount.setUsername(username);
        customerAccount.setPassword(password);
        iCustomerAccountService.save(customerAccount);
        return "redirect:/home";

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
                                 @RequestParam(value = "password", required = false) String password,
                                 @RequestParam(value = "rePassword", required = false) String rePassword,
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
}
