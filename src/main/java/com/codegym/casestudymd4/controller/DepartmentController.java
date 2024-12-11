package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Department;
import com.codegym.casestudymd4.model.Staff;
import com.codegym.casestudymd4.repository.IDepartmentRepository;
import com.codegym.casestudymd4.service.IDepartmentService;
import com.codegym.casestudymd4.service.implement.DepartmentService;
import com.codegym.casestudymd4.service.implement.StaffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    private final IDepartmentService iDepartmentService;
    private final DepartmentService departmentService;
    private final StaffService staffService;
    public DepartmentController(IDepartmentService iDepartmentService, DepartmentService departmentService, StaffService staffService){
        this.iDepartmentService = iDepartmentService;
        this.departmentService = departmentService;
        this.staffService = staffService;
    }

    @GetMapping("/list")
    public ModelAndView getDepartmentList(@RequestParam(name = "idDepartment", required = false) Long idDepartment){
        Iterable<Department> departmentList = iDepartmentService.findAll();
        ModelAndView modelAndView = new ModelAndView("department/list");
        if (!departmentList.iterator().hasNext()) {
            modelAndView.addObject("errorMessage", "Chưa có phòng ban nào trong hệ thống.");
            modelAndView.addObject("departmentList", departmentList);
            modelAndView.addObject("staffs", List.of());
        }
        Long selectDepartmentId = (idDepartment != null) ? idDepartment : departmentList.iterator().next().getId();
        List<Staff> staffs = staffService.getStaffsByDepartment(selectDepartmentId);
        if (staffs.isEmpty()){
            modelAndView.addObject("message", "Không có nhân sự nào!");
            modelAndView.addObject("departmentList", departmentList);
            modelAndView.addObject("staffs",staffs);
            modelAndView.addObject("selectDepartmentId", selectDepartmentId);
        }
        modelAndView.addObject("departmentList", departmentList);
        modelAndView.addObject("staffs",staffs);
        modelAndView.addObject("selectDepartmentId", selectDepartmentId);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(){
        ModelAndView modelAndView = new ModelAndView("department/add");
        modelAndView.addObject("department", new Department());
        return modelAndView;
    }

    @PostMapping("/add")
    public String addDepartment(@Validated @ModelAttribute Department department, BindingResult bindingResult, @RequestParam String name, Model model) {
        if (bindingResult.hasErrors()){
            return "department/add";
        }
        if(departmentService.existBrandDepartment(name)){
            model.addAttribute("errorMessage", "Phòng ban đã tồn tại!");
            return "department/add";
        }
        iDepartmentService.save(department);
        return "redirect:/departments/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<Department> departmentOptional = iDepartmentService.findById(id);
        if (departmentOptional.isPresent()){
            ModelAndView modelAndView = new ModelAndView("department/edit");
            modelAndView.addObject("department", departmentOptional.get());
            return modelAndView;
        } else {
            return new ModelAndView("department/list");
        }
    }

    @PostMapping("/edit")
    public String editDepartment(@RequestParam Long id, @Validated @ModelAttribute Department department, BindingResult bindingResult, @RequestParam String name, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            return "redirect:/departments/edit?id=" + id;
        }
        Optional<Department> departmentOptional = iDepartmentService.findById(id);
        Department existDepartment = departmentOptional.get();
        if (!existDepartment.getName().equals(name)){
            if(departmentService.existBrandDepartment(name)){
                redirectAttributes.addFlashAttribute("errorMessage", "Phòng ban đã tồn tại!");
                return "redirect:/departments/edit?id=" + id;
            }
        }
        iDepartmentService.save(department);
        return "redirect:/departments/list";
    }

    @GetMapping("/delete")
    public String deleteDepartment (@RequestParam Long id){
        iDepartmentService.delete(id);
        return "redirect:/departments/list";
    }
}
