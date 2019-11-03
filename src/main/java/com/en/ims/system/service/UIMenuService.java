package com.en.ims.system.service;


import com.en.ims.system.entity.SystemUiMenu;
import com.en.ims.system.repository.UIMenuRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class UIMenuService {
    @Autowired
    private UIMenuRepository menuRepository;

    public Page<SystemUiMenu> findAll(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    public List<SystemUiMenu> findByModuleId(Integer moduleId) {
        List<SystemUiMenu> list = menuRepository.findByModuleIdOrderByOrderNo(moduleId);
        HashMap<String, List<SystemUiMenu>> map = Maps.newHashMap();
        for(SystemUiMenu menu : list){
            List<SystemUiMenu> menus;
            if(map.containsKey(menu.getSuperId())){
                menus = map.get(menu.getSuperId());
            }else{
                menus = Lists.newArrayList();
            }
            menus.add(menu);
            map.put(menu.getSuperId(),menus);
        }
        if(map.isEmpty()){
            return  list;
        }
        List<SystemUiMenu> menus = map.get("#");
        getMenu(menus,map);
        return menus;

    }
    private void getMenu(List<SystemUiMenu> list, Map<String,List<SystemUiMenu>> map) {
        for (SystemUiMenu menu : list) {
            String superid = menu.getId()+"";
            if (map.containsKey(superid)) {
                List<SystemUiMenu> menusList = map.get(superid);
                menu.setChildren(menusList);
                getMenu(menusList, map);
            }
        }
    }
}
