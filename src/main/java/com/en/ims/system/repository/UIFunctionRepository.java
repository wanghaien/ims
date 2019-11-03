package com.en.ims.system.repository;

import com.en.ims.common.repository.CommonRepository;
import com.en.ims.system.entity.SystemUiFunction;
import java.util.List;

public interface UIFunctionRepository extends CommonRepository<SystemUiFunction,Integer> {
    List<SystemUiFunction> findByEntityOrderByOrderNo(String entity);
}
