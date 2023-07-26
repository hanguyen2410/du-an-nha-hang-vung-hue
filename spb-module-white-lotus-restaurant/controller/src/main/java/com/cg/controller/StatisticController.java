package com.cg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stats")
public class StatisticController {

    @GetMapping("/product")
    public String showStatisticProduct() {
        return "statistic/product";
    }
}
