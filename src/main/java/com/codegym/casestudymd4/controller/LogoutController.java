package com.codegym.casestudymd4.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class LogoutController {
    @GetMapping
    public String logout(HttpSession session){
        if (session!=null){
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/staff")
    public String logoutStaff(HttpSession session){
        if (session!=null){
            session.invalidate();
        }
        return "redirect:/staff_account/login";
    }
}
