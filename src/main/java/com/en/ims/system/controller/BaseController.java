package com.en.ims.system.controller;

import com.en.ims.system.entity.SystemUiFunction;
import com.en.ims.system.entity.SystemUiModule;
import com.en.ims.system.entity.SystemUiTable;
import com.en.ims.system.repository.UserRepository;
import com.en.ims.system.service.UIFunctionService;
import com.en.ims.system.service.UIModuleService;
import com.en.ims.system.service.UITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/index")
public class BaseController {

    @Autowired
    private UIModuleService moduleService;
    @Autowired
    private UIFunctionService functionService;
    @Autowired
    private UITableService tableService;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("")
    public String index(Map<String,Object> map){
        List<SystemUiModule> modules = moduleService.find();
        map.put("modules",modules);
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        map.put("username",userRepository.findByUserName(user.getUsername()).getRealName());
        return "index";
    }

    @RequestMapping("/{entity}")
    public String indexEntity(@PathVariable String entity, Model model){
        List<SystemUiFunction> functions = functionService.findByEntityOrderByOrderNo(entity);
        List<SystemUiTable> UITables = tableService.findByEntityOrderByOrderNo(entity);
        model.addAttribute("functions",functions);
        model.addAttribute("UITables",UITables);
        model.addAttribute("entity",entity);
        return entity + "/main";
    }
}
