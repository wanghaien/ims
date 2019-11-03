package com.en.ims.system.repository;



import com.en.ims.common.repository.CommonRepository;
import com.en.ims.system.entity.SystemUiMenu;
import java.util.List;

public interface UIMenuRepository extends CommonRepository<SystemUiMenu,Integer> {
    List<SystemUiMenu> findByModuleIdOrderByOrderNo(Integer moduleId);
}
