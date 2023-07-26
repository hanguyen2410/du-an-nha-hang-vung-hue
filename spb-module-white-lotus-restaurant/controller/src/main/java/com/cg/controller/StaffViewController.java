package com.cg.controller;

import com.cg.domain.entity.Role;
import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.unit.Unit;
import com.cg.service.category.ICategoryService;
import com.cg.service.role.IRoleService;
import com.cg.service.unit.IUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/home/staff")
public class StaffViewController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUnitService iUnitService;
    @Autowired
    private IRoleService roleService;
    @GetMapping()
    public ModelAndView showStaffHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("staff-template/home");
        return modelAndView;
    }
    @GetMapping("/products")
    public ModelAndView showListPageStaffView() {
        ModelAndView modelAndView = new ModelAndView();
        List<Category> categories = categoryService.findAll();
        List<Unit> units = iUnitService.findAll();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("units", units);
        modelAndView.setViewName("staff-template/productList");
        return modelAndView;
    }
    @GetMapping("/staffs")
    public ModelAndView getListStaffs() {
        ModelAndView modelAndView = new ModelAndView();
        List<Role> roles = roleService.findAll();
        modelAndView.addObject("roles", roles);
        modelAndView.setViewName("staff-template/staffList");
        return modelAndView;
    }
    @GetMapping("/tables")
    public String showTablePage() {
        return "staff-template/tableList";
    }
    @GetMapping("/stats/product")
    public String showStatisticProduct() {
        return "staff-template/productStatistic";
    }
}
