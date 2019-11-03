package com.en.ims.system.controller;




import com.en.ims.common.entity.RequestPage;
import com.en.ims.common.entity.ResponsePage;
import com.en.ims.system.entity.SystemUiMenu;
import com.en.ims.system.service.UIMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private UIMenuService menuService;

    @Autowired
    private ResponsePage responsePage;

    @RequestMapping("/list/{moduleId}")
    @ResponseBody
    public List<SystemUiMenu> find(@PathVariable Integer moduleId, Map<String,Object> map){
        return menuService.findByModuleId(moduleId);
    }
    @RequestMapping("/listPage")
    @ResponseBody
    public ResponsePage<SystemUiMenu> listPage(Model model, RequestPage requestPage){
        String sortType = requestPage.getOrder()!=null ? requestPage.getOrder():"desc";
        String sortFiled = requestPage.getSort()!=null?requestPage.getSort():"id";
        Sort sort = Sort.by(sortType.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortFiled);
        Pageable pageable = PageRequest.of(requestPage.getPage()-1, requestPage.getRows(), sort);
        Page<SystemUiMenu> menus = menuService.findAll(pageable);
        responsePage.setTotal(menus.getTotalElements());
        responsePage.setRows(menus.getContent());
        return responsePage;
    }
}
