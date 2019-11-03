package com.en.ims.system.service;


import com.en.ims.system.entity.SystemUiTable;
import com.en.ims.system.repository.UITableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UITableService {
    @Autowired
    private UITableRepository repository;

    public List<SystemUiTable> findByEntityOrderByOrderNo(String entity) {
        return repository.findByEntityOrderByOrderNo(entity);
    }
}
