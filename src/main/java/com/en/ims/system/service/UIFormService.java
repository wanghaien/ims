package com.en.ims.system.service;


import com.en.ims.system.entity.SystemUiForm;
import com.en.ims.system.repository.UIFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UIFormService{
    @Autowired
    private UIFormRepository repository;

    public SystemUiForm findById(String id) {
        return repository.findById(id);
    }
}
