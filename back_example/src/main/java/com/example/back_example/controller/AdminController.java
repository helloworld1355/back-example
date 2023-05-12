package com.example.back_example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @RequestMapping("/index")
    public String index(){
        return "admin/index";
    }

    // 登錄頁面
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView view = new ModelAndView(); //視圖
        view.setViewName("admin/login.html"); // 設置模板的路徑
        return view;
    }


}
