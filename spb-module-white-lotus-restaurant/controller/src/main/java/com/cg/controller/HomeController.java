package com.cg.controller;

import com.cg.domain.dto.user.UserInfoDTO;
import com.cg.domain.entity.Role;
import com.cg.domain.entity.User;
import com.cg.exception.UnauthorizedException;
import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    private AppUtils appUtils;
    @Autowired
    private IUserService userService;

    @GetMapping
    public String showHomePage(Model model) {
        userService.getInfo(model);
        return "home";
    }

    @GetMapping("/temp")
    public String showTempPage() {
        return "temp";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login/login";
    }

    @GetMapping("/forgot")
    public String showForgotPage() {
        return "login/forgot";
    }

    @GetMapping("/report")
    public String showReportPage(Model model) {
        userService.getInfo(model);
        return "report/report";
    }
    @GetMapping("/daily")
    public String showDailyPage(Model model) {
        userService.getInfo(model);
        return "report/dailyReport";
    }

}
