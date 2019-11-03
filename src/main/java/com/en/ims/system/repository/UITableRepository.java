package com.en.ims.system.repository;


import com.en.ims.common.repository.CommonRepository;
import com.en.ims.system.entity.SystemUiTable;

import java.util.List;

public interface UITableRepository extends CommonRepository<SystemUiTable,Integer> {
    List<SystemUiTable> findByEntityOrderByOrderNo(String entity);
}
