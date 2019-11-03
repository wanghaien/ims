package com.en.ims.system.controller;

import com.en.ims.common.entity.RequestPage;
import com.en.ims.common.entity.ResponsePage;
import com.en.ims.system.entity.SystemUser;
import com.en.ims.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ResponsePage responsePage;

    @GetMapping("/listPage")
    @ResponseBody
    public ResponsePage<SystemUser>  listPage(RequestPage requestPage){
        String sortType = requestPage.getOrder() != null ? requestPage.getOrder():"desc";
        String sortFiled = requestPage.getSort() != null ? requestPage.getSort():"id";
        Sort sort = Sort.by(sortType.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortFiled);
        Pageable pageable = PageRequest.of(requestPage.getPage()-1, requestPage.getRows(), sort);
        Page<SystemUser> users = userService.findAll(pageable);
        responsePage.setTotal(users.getTotalElements());
        responsePage.setRows(users.getContent());
        return responsePage;
    }
}
