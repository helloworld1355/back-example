package com.example.back_example.controller;

import com.example.back_example.common.CommonResult;
import com.example.back_example.entity.User;
import com.example.back_example.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/index")
    public String index(){
        return "user/index";
    }

    @GetMapping("/user/form")
    public ModelAndView form(Integer id, Model model){
        User user;
        if(id==0){
            user = new User();
            user.setId(0);
        }
        else{
            user = userService.getUserById(id);
        }
        ModelAndView view = new ModelAndView();
        view.setViewName("user/form.html");
        model.addAttribute("model",user);
        return view;
    }

    @ResponseBody
    @RequestMapping("/user/list")
    public CommonResult<List<User>> list(int page, int limit, String username, String realname){

        CommonResult<List<User>> rs = userService.search(username,realname,page,limit);
        return rs;
    }

    @ResponseBody
    @PostMapping("/user/save")
    public CommonResult<Boolean> save(User user){
        User model = user;
        if(model.getId()==0){
            model.setUserPwd("123456");
            model.setIsDel(0);
        }else{
            model = userService.getUserById(model.getId());
            model.setUserName(user.getUserName());
            model.setEmail(user.getEmail());
            model.setTel(user.getTel());
            model.setRealName(user.getRealName());
        }
        return userService.save(model);
    }

    @ResponseBody
    @PostMapping("/user/del")
    public CommonResult<Boolean> del(Integer id){
        return userService.delete(id);
    }

    @ResponseBody
    @PostMapping("/user/reset_password")
    public CommonResult<Boolean> resetPassword(Integer id){
        return userService.resetPassword(id);
    }

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<User> login(String username, String password, HttpSession session){
        System.out.println(username + password);
        CommonResult<User> rs = userService.login(username,password);
        if(rs.getCode()==0){
            session.setAttribute("user",rs.getData());
        }else{

        }
        return rs;

    }

    @RequestMapping("/user/logout")
    @ResponseBody
    public CommonResult<Integer> logout(HttpSession session){
        session.removeAttribute("user");
        CommonResult rs = new CommonResult();
        rs.setCode(0);
        rs.setMsg("注销成功");
        return rs;
    }

    @GetMapping("/user/changepwd")
    public ModelAndView changePwd(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/changepwd");
        return view;
    }

    @ResponseBody
    @PostMapping("/user/changepwd")
    public CommonResult changeNewPwd(HttpSession session,String old_pwd,String new_pwd){
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        return userService.changePwd(user.getId(),old_pwd,new_pwd);
    }


}
