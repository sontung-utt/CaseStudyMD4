package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.CustomerAccount;
import com.codegym.casestudymd4.model.Role;
import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.model.StaffAccountPrinciple;
import com.codegym.casestudymd4.service.IRoleService;
import com.codegym.casestudymd4.service.IStaffAccountService;
import com.codegym.casestudymd4.service.implement.StaffAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/staff_account")
public class StaffAccountController {
    private final IStaffAccountService iStaffAccountService;
    private final StaffAccountService staffAccountService;
    private final IRoleService iRoleService;

    @Autowired
    public StaffAccountController(IStaffAccountService iStaffAccountService, StaffAccountService staffAccountService, IRoleService iRoleService){
        this.iStaffAccountService = iStaffAccountService;
        this.staffAccountService = staffAccountService;
        this.iRoleService = iRoleService;
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(){
        ModelAndView modelAndView = new ModelAndView("staff_account/login");
        modelAndView.addObject("user",new StaffAccount());
        return modelAndView;
    }

    @PostMapping("/login")
    public String loginStaff(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpSession session,
                             Model model) {
        Long idLoginStaff = staffAccountService.getIdByUsername(username);
        if (staffAccountService.checkUser(username, password)) {
            session.setAttribute("username", username);
            session.setAttribute("idLoginStaff", idLoginStaff);
            return "redirect:/products/list";
        } else {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "staff_account/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(){
        return "staff_account/register";
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @GetMapping("/list")
    public ModelAndView getStaffAccountList(){
        Iterable<StaffAccount> staffAccountList = iStaffAccountService.findAll();

        ModelAndView modelAndView = new ModelAndView("staff_account/list");
        if (!staffAccountList.iterator().hasNext()){
            modelAndView.addObject("errorMessage","Chưa có tài khoản nhân sự nào trong hệ thống");
            modelAndView.addObject("staffAccountList", staffAccountList);
            return modelAndView;
        }
        for (StaffAccount account : staffAccountList) {
            account.setCreated_at(LocalDateTime.parse(formatLocalDateTime(account.getCreated_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            account.setModified_at(LocalDateTime.parse(formatLocalDateTime(account.getModified_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

            account.setFormattedCreatedAt(formatLocalDateTime(account.getCreated_at()));
            account.setFormattedModifiedAt(formatLocalDateTime(account.getModified_at()));
        }

        modelAndView.addObject("staffAccountList", staffAccountList);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(){
        ModelAndView modelAndView = new ModelAndView("staff_account/add");
        Iterable<Role> roleList = iRoleService.findAll();
        modelAndView.addObject("staffAccount", new StaffAccount());
        modelAndView.addObject("roleList", roleList);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addStaffAccount(@Validated @ModelAttribute StaffAccount staffAccount, BindingResult bindingResult, @RequestParam(required = false) Long idRole, @RequestParam("username") String username, Model model){
        Iterable<Role> roleList = iRoleService.findAll();
        Optional<Role> roleOptional = iRoleService.findById(idRole);
        if (bindingResult.hasErrors()){
            model.addAttribute("staffAccount",staffAccount);
            model.addAttribute("roleList", roleList);
            return "staff_account/add";
        }
        if (!roleOptional.isPresent()) {
            model.addAttribute("staffAccount", staffAccount);
            model.addAttribute("roleList", roleList);
            model.addAttribute("errorMessage", "Vai trò không hợp lệ!");
            return "staff_account/add";
        }
        if(staffAccountService.existStaffAccount(username)){
            model.addAttribute("errorMessage", "Tên tài khoản đã tồn tại!");
            model.addAttribute("roleList", roleList);
            return "staff_account/add";
        }

        staffAccount.setRole(roleOptional.get());
        iStaffAccountService.save(staffAccount);
        return "redirect:/staff_account/list";
    }

    @GetMapping("/edit_account")
    public ModelAndView showFormEditAccount(@RequestParam Long id){
        Optional<StaffAccount> optionalStaffAccount = iStaffAccountService.findById(id);
        StaffAccount staffAccount = optionalStaffAccount.get();
        ModelAndView modelAndView = new ModelAndView("staff_account/edit_account");
        modelAndView.addObject("staffAccount", staffAccount);
        return modelAndView;
    }

    @PostMapping("/edit_account")
    public String editAccount(@RequestParam Long id,
                              @ModelAttribute StaffAccount staffAccount,
                              @RequestParam String username,
                              RedirectAttributes redirectAttributes){
        Optional<StaffAccount> optionalStaffAccount = iStaffAccountService.findById(id);
        StaffAccount existStaffAccount = optionalStaffAccount.get();
        if (!existStaffAccount.getUsername().equals(username)){
            if (staffAccountService.existStaffAccount(username)){
                redirectAttributes.addFlashAttribute("errorMessage", "Tên tài khoản đã tồn tại!");
                return "redirect:/staff_account/edit_account?id=" + id;
            }
        }
        staffAccount.setCreated_at(existStaffAccount.getCreated_at());
        staffAccount.setRole(existStaffAccount.getRole());
        iStaffAccountService.save(staffAccount);
        return "redirect:/go-back";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<StaffAccount> staffAccountOptional = iStaffAccountService.findById(id);
        Iterable<StaffAccount> staffAccountList = iStaffAccountService.findAll();
        ModelAndView modelAndView = new ModelAndView("staff_account/edit");
        Iterable<Role> roleList = iRoleService.findAll();
        if (!staffAccountOptional.isPresent()){
            modelAndView = new ModelAndView("product/list");
            modelAndView.addObject("message", "Không tìm thấy mã tài khoản "+ id + "!");
            modelAndView.addObject("staffAccountList", staffAccountList);
            return modelAndView;
        }
        modelAndView.addObject("roleList", roleList);
        modelAndView.addObject("staffAccount", staffAccountOptional.get());
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editStaffAccount(@RequestParam Long id, @Validated @ModelAttribute StaffAccount staffAccount, BindingResult bindingResult, @RequestParam(required = false) Long idRole, @RequestParam String username, RedirectAttributes redirectAttributes, Model model){
        Iterable<Role> roleList = iRoleService.findAll();
        Optional<StaffAccount> staffAccountOptional = iStaffAccountService.findById(id);
        if (bindingResult.hasErrors()){
            return "redirect:/staff_account/edit?id=" + id;
//            model.addAttribute("staffAccount", staffAccountOptional.get());
//            model.addAttribute("roleList", roleList);
//            return "staff_account/edit";
        }
        if (!staffAccountOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid staff account Id:" + id);
        }
        Optional<Role> roleOptional = iRoleService.findById(idRole);
        StaffAccount existStaffAccount = staffAccountOptional.get();
        if (!existStaffAccount.getUsername().equals(username)){
            if (staffAccountService.existStaffAccount(username)){
                redirectAttributes.addFlashAttribute("errorMessage", "Tên tài khoản đã tồn tại!");
                model.addAttribute("roleList", roleList);
                return "redirect:/staff_account/edit?id=" + id;
            }
        }
        if (!roleOptional.isPresent()) {
            model.addAttribute("staffAccount", staffAccount);
            model.addAttribute("roleList", roleList);
            model.addAttribute("errorMessage", "Vai trò không hợp lệ!");
            return "staff_account/add";
        }
        staffAccount.setCreated_at(existStaffAccount.getCreated_at());
        staffAccount.setRole(roleOptional.get());
        iStaffAccountService.save(staffAccount);
        return "redirect:/staff_account/list";
    }

    @GetMapping("/delete")
    public String deleteStaffAccount (@RequestParam Long id){
        iStaffAccountService.delete(id);
        return "redirect:/staff_account/list";
    }
}
