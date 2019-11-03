package com.en.ims.system.entity;

import com.en.ims.common.entity.BaseTable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Data
@Table(name="system_user")
public class SystemUser extends BaseTable {
    private String userName;
    private String realName;
    private String passWord;
    @Length(max=11)
    private String telephone;
    @Email
    private String email;
    private Integer sex;
    private String idNumber;
    private String address;
    private Integer status;


}
