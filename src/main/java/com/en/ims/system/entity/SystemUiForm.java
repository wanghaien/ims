package com.en.ims.system.entity;


import com.en.ims.common.entity.BaseTable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name="system_ui_form")
public class SystemUiForm extends BaseTable {
    private String orderNo;

}
