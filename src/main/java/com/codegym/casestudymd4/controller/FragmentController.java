package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.configuration.SecurityUtils;
import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.model.StaffAccountPrinciple;
import com.codegym.casestudymd4.service.implement.StaffAccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/fragment")
public class FragmentController {
    private final StaffAccountService staffAccountService;
    public FragmentController(StaffAccountService staffAccountService){
        this.staffAccountService = staffAccountService;
    }

    @GetMapping("/top_frame")
    public String topFrame(Model model) {
//        if (userDetails == null) {
//            return "redirect:/staff_account/login";
//        }
        String username = SecurityUtils.getLoggedInUsername();
        Long id = staffAccountService.getIdByUsername(username);
        model.addAttribute("username", username);
        model.addAttribute("id", id);
        return "fragments/topFrame";
    }

    @GetMapping("/left_frame")
    public String leftFrame(Model model, @AuthenticationPrincipal StaffAccountPrinciple userDetails){
        if (userDetails == null) {
            return "redirect:/staff_account/login";
        }

        model.addAttribute("roleName", userDetails.getAuthorities());
        return "fragments/leftFrame";
    }
}
