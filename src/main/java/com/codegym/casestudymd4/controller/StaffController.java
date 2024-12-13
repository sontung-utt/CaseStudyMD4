package com.codegym.casestudymd4.controller;

import com.codegym.casestudymd4.service.IStaffService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staffs")
public class StaffController {
    @Value("${file-upload}")
    private String uploadFile;

    private final IStaffService iStaffService;
    public StaffController(IStaffService iStaffService){
        this.iStaffService = iStaffService;
    }
}
