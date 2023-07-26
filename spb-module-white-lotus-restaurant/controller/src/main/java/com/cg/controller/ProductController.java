package com.cg.controller;

import com.cg.domain.entity.category.Category;
import com.cg.domain.entity.unit.Unit;
import com.cg.service.category.ICategoryService;
import com.cg.service.unit.IUnitService;
import com.cg.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUnitService iUnitService;

    @Autowired
    private IUserService userService;

    @GetMapping
    public String showListPage(Model model) {
        List<Category> categories = categoryService.findAll();
        List<Unit> units = iUnitService.findAll();
        userService.getInfo(model);
        model.addAttribute("categories", categories);
        model.addAttribute("units", units);
        return "product/list";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        List<Category> categories = categoryService.findAll();
        List<Unit> units = iUnitService.findAll();
        userService.getInfo(model);
        model.addAttribute("categories", categories);
        model.addAttribute("units", units);
        return "product/create";
    }

}
