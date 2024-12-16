package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Role;
import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.service.IRoleService;
import com.codegym.casestudymd4.service.implement.RoleService;
import com.codegym.casestudymd4.service.implement.StaffAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/role")
public class RoleController {
    private final IRoleService iRoleService;
    private final RoleService roleService;
    private final StaffAccountService staffAccountService;
    @Autowired
    public RoleController(IRoleService iRoleService, RoleService roleService, StaffAccountService staffAccountService){
        this.iRoleService = iRoleService;
        this.roleService = roleService;
        this.staffAccountService = staffAccountService;
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @GetMapping("/list")
    public ModelAndView getRoleList(@RequestParam(value = "idRole",required = false) Long idRole){
        Iterable<Role> roleList = iRoleService.findAll();
        ModelAndView modelAndView = new ModelAndView("role/list");
        if (!roleList.iterator().hasNext()){
            modelAndView.addObject("errorMessage", "Chưa có chức năng nào trong hệ thống.");
            modelAndView.addObject("roleList", roleList);
            modelAndView.addObject("staffAccounts", List.of());
            return modelAndView;
        }
        Long selectRoleId = (idRole != null) ? idRole : roleList.iterator().next().getId();
        List<StaffAccount> staffAccounts = staffAccountService.getStaffAccountsByRole(idRole);
        if (staffAccounts.isEmpty()){
            modelAndView.addObject("message", "Không có tài khoản nhân sự nào!");
            modelAndView.addObject("roleList", roleList);
            modelAndView.addObject("staffAccounts",staffAccounts);
            modelAndView.addObject("selectRoleId", selectRoleId);
        }
        for (StaffAccount account : staffAccounts) {
            account.setCreated_at(LocalDateTime.parse(formatLocalDateTime(account.getCreated_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            account.setModified_at(LocalDateTime.parse(formatLocalDateTime(account.getModified_at()), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

            account.setFormattedCreatedAt(formatLocalDateTime(account.getCreated_at()));
            account.setFormattedModifiedAt(formatLocalDateTime(account.getModified_at()));
        }
        modelAndView.addObject("roleList",roleList);
        modelAndView.addObject("staffAccounts",staffAccounts);
        modelAndView.addObject("selectRoleId", selectRoleId);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(){
        ModelAndView modelAndView = new ModelAndView("role/add");
        modelAndView.addObject("role", new Role());
        return modelAndView;
    }

    @PostMapping("/add")
    public String addRole(@Validated @ModelAttribute Role role, BindingResult bindingResult, @RequestParam String name, Model model){
        if (bindingResult.hasErrors()){
            return "role/add";
        }
        if(roleService.existRoleName(name)){
            model.addAttribute("errorMessage", "Chức năng đã tồn tại!");
            return "role/add";
        }
        iRoleService.save(role);
        return "redirect:/role/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<Role> roleOptional = iRoleService.findById(id);
        if (roleOptional.isPresent()){
            ModelAndView modelAndView = new ModelAndView("role/edit");
            modelAndView.addObject("role", roleOptional.get());
            return modelAndView;
        } else {
            return new ModelAndView("role/list");
        }
    }

    @PostMapping("/edit")
    public String editRole(@RequestParam Long id, @Validated @ModelAttribute Role role, BindingResult bindingResult,@RequestParam String name , RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            return "redirect:/role/edit?id=" + id;
        }
        Optional<Role> roleOptional = iRoleService.findById(id);
        Role existRole = roleOptional.get();
        if (!existRole.getName().equals(name)){
            if (roleService.existRoleName(name)){
                redirectAttributes.addFlashAttribute("errorMessage", "Chức năng đã tồn tại!");
                return "redirect:/role/edit?id=" + id;
            }
        }
        iRoleService.save(role);
        return "redirect:/role/list";
    }

    @GetMapping("/delete")
    public String deleteRole (@RequestParam Long id){
        iRoleService.delete(id);
        return "redirect:/role/list";
    }
}
