package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.model.Department;
import com.codegym.casestudymd4.model.Staff;
import com.codegym.casestudymd4.model.StaffAccount;
import com.codegym.casestudymd4.model.form.StaffForm;
import com.codegym.casestudymd4.service.IDepartmentService;
import com.codegym.casestudymd4.service.IStaffAccountService;
import com.codegym.casestudymd4.service.IStaffService;
import com.codegym.casestudymd4.service.implement.DepartmentService;
import com.codegym.casestudymd4.service.implement.StaffAccountService;
import com.codegym.casestudymd4.service.implement.StaffService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/staffs")
public class StaffController {
    private final StaffService staffService;
    @Value("${file-upload}")
    private String uploadFile;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final IStaffService iStaffService;
    private final StaffAccountService staffAccountService;
    private final IStaffAccountService iStaffAccountService;
    private final IDepartmentService iDepartmentService;
    public StaffController(IStaffService iStaffService, IDepartmentService iDepartmentService, StaffAccountService staffAccountService, IStaffAccountService iStaffAccountService, StaffService staffService){
        this.iStaffService = iStaffService;
        this.iDepartmentService = iDepartmentService;
        this.staffAccountService = staffAccountService;
        this.iStaffAccountService = iStaffAccountService;
        this.staffService = staffService;
    }
    @GetMapping("/list")
    public ModelAndView showStaffList(){
        Iterable<Staff> staffList = iStaffService.findAll();
        ModelAndView modelAndView = new ModelAndView("staff/list");
        if (!staffList.iterator().hasNext()){
            modelAndView.addObject("errorMessage", "Chưa có nhân sự nào trong hệ thống.");
            modelAndView.addObject("staffList", staffList);
            return modelAndView;
        }
        modelAndView.addObject("staffList", staffList);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd(){
        Iterable<Department> departments = iDepartmentService.findAll();
        ModelAndView modelAndView = new ModelAndView("staff/add");
        modelAndView.addObject("staffForm", new StaffForm());
        modelAndView.addObject("departments", departments);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addStaff(@Validated @ModelAttribute(name = "staffForm") StaffForm staffForm,
                           BindingResult bindingResult,
                           @RequestParam(required = false) Long idDepartment,
                           //@RequestParam Long idUser,
                           @RequestParam String username,
                           @RequestParam String phone,
                           @RequestParam String email,
                           Model model) {
        Iterable<Department> departments = iDepartmentService.findAll();
        if (bindingResult.hasErrors()){
            model.addAttribute("departments", departments);
            return "staff/add";
        }
        MultipartFile multipartFile = staffForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(staffForm.getImage().getBytes(), new File(uploadFile + fileName));
        } catch (IOException e){
            e.printStackTrace();
        }
        Long idUser = staffAccountService.getIdByUsername(username);
        if (idUser == null) {
            model.addAttribute("errorMessage", "Tài khoản không tồn tại!");
            model.addAttribute("departments", departments);
            return "staff/add";
        }
        Optional<Department> departmentOptional = iDepartmentService.findById(idDepartment);
        Optional<StaffAccount> staffAccountOptional = iStaffAccountService.findById(idUser);
        if (!departmentOptional.isPresent()){
            model.addAttribute("errorMessage", "Phòng ban không tồn tại!");
            model.addAttribute("departments", departments);
            return "staff/add";
        }

        if (staffService.usedStaffAccount(idUser)){
            model.addAttribute("errorMessage", "Tài khoản đã được sử dụng!");
            model.addAttribute("departments", departments);
            return "staff/add";
        }

        if(staffService.existPhone(phone)){
            model.addAttribute("errorMessage", "Số điện thoại đã được sử dụng!");
            model.addAttribute("departments", departments);
            return "staff/add";
        }

        if(staffService.existEmail(email)){
            model.addAttribute("errorMessage", "Email đã được sử dụng!");
            model.addAttribute("departments", departments);
            return "staff/add";
        }

        Staff staff = new Staff(staffForm.getId(), staffForm.getName(), staffForm.getGender(),staffForm.getBirth(), fileName, staffForm.getAddress(), email, phone, staffForm.getSalary(), departmentOptional.get(), staffAccountOptional.get());
        iStaffService.save(staff);
        return "redirect:/staffs/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        Optional<Staff> staffOptional = iStaffService.findById(id);
        Iterable<Staff> staffList = iStaffService.findAll();
        Iterable<Department> departments = iDepartmentService.findAll();
        ModelAndView modelAndView = new ModelAndView("staff/edit");
        if (!staffOptional.isPresent()){
            modelAndView = new ModelAndView("staff/list");
            modelAndView.addObject("message", "Không tìm thấy mã nhân sự "+ id + "!");
            modelAndView.addObject("staffList", staffList);
            return modelAndView;
        }
        Staff staff = staffOptional.get();
        StaffForm staffForm = new StaffForm();
        staffForm.setId(staff.getId());
        staffForm.setName(staff.getName());
        staffForm.setBirth(staff.getBirth());
        staffForm.setGender(staff.getGender());
        staffForm.setAddress(staff.getAddress());
        staffForm.setEmail(staff.getEmail());
        staffForm.setPhone(staff.getPhone());
        staffForm.setOldImage(staff.getImage());
        staffForm.setSalary(staff.getSalary());
        staffForm.setDepartment(staff.getDepartment());
        staffForm.setStaffAccount(staff.getStaffAccount());

        modelAndView.addObject("staffForm", staffForm);
        modelAndView.addObject("departments", departments);
        modelAndView.addObject("staff",staff);
        return modelAndView;
    }
}
