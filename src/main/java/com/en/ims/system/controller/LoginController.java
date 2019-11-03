package com.en.ims.system.controller;


import com.en.ims.system.entity.SystemUser;
import com.en.ims.system.service.UIMenuService;
import com.en.ims.system.service.UIModuleService;
import com.en.ims.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private UIModuleService moduleService;
    @Autowired
    private UIMenuService menuService;

    @RequestMapping("")
    public String home(){
        return "redirect:/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(SystemUser user, Model model){
        return "login";
    }
    @RequestMapping(value = "/loginError")
    public String loginFail(Model model){
        model.addAttribute("loginError", true);
        return "login";
    }
}
