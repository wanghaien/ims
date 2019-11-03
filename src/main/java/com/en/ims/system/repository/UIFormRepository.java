package com.en.ims.system.repository;


import com.en.ims.common.repository.CommonRepository;
import com.en.ims.system.entity.SystemUiForm;

public interface UIFormRepository extends CommonRepository<SystemUiForm,Integer> {
    SystemUiForm findById(String id);
}
