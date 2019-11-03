package com.en.ims.system.service;


import com.en.ims.system.entity.SystemUiModule;
import com.en.ims.system.repository.UIModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UIModuleService {
    @Autowired
    private UIModuleRepository moduleRepository;

    public List<SystemUiModule> find() {
        return moduleRepository.findAll();
    }
}
