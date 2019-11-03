package com.en.ims.system.repository;


import com.en.ims.common.repository.CommonRepository;
import com.en.ims.system.entity.SystemUser;

public interface UserRepository extends CommonRepository<SystemUser,Integer> {
    SystemUser findByUserNameAndPassWord(String userName, String passWord);

    SystemUser findByUserName(String username);
}
