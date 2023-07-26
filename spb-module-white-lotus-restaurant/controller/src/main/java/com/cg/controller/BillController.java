package com.cg.controller;

import com.cg.domain.entity.Role;
import com.cg.service.role.IRoleService;
import com.cg.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/bills")
public class BillController {
    @Autowired
    private IUserService userService;

    @GetMapping("")
    public String getListBills(Model model) {
        userService.getInfo(model);
        return "bill/list";
    }
    @GetMapping("/{billId}")
    public String getDetailBills(Model model) {
        userService.getInfo(model);
        return "bill/view";
    }

    @GetMapping("/day/{day}")
    public String getListBillsByDay(Model model) {
        userService.getInfo(model);
        return "bill/viewByDay";

    }
}
