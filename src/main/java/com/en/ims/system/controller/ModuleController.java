package com.en.ims.system.controller;


import com.en.ims.system.entity.SystemUiModule;
import com.en.ims.system.service.UIModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/module")
public class ModuleController {

    @Autowired
    private UIModuleService moduleService;

    @RequestMapping("/list")
    public List<SystemUiModule> list(){
        return moduleService.find();
    }
}
