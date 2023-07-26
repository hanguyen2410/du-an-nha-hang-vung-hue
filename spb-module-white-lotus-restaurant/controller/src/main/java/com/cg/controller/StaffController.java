package com.cg.controller;

import com.cg.domain.entity.Role;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.unit.Unit;
import com.cg.service.category.ICategoryService;
import com.cg.service.role.IRoleService;
import com.cg.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/staffs")
public class StaffController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;
    @GetMapping("")
    public String getListStaffs(Model model) {
        List<Role> roles = roleService.findAll();
        userService.getInfo(model);
        model.addAttribute("roles", roles);
        return "staff/listStaff";
    }
    @GetMapping("/create")
    public String createNewStaff(Model model) {
        List<Role> roles = roleService.findAll();
        userService.getInfo(model);
        model.addAttribute("roles", roles);
        return "staff/create";
    }
}
