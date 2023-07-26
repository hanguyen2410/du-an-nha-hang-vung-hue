package com.cg.controller;

import com.cg.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private IUserService userService;
    @GetMapping("")
    public String showTablePage(Model model) {
        userService.getInfo(model);
        return "table/list";
    }
    @GetMapping("/createTable")
    public String showCreateTableForm(Model model) {
        userService.getInfo(model);
        return "table/create";
    }
}
