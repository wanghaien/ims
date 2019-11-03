package com.en.ims.system.service;


import com.en.ims.system.entity.SystemUiFunction;
import com.en.ims.system.repository.UIFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UIFunctionService {
    @Autowired
    private UIFunctionRepository repository;

    public List<SystemUiFunction> findByEntityOrderByOrderNo(String entity) {
        return repository.findByEntityOrderByOrderNo(entity);
    }
}
