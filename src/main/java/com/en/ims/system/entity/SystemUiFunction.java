package com.en.ims.system.entity;

import com.en.ims.common.entity.BaseTable;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="system_ui_function")
public class SystemUiFunction extends BaseTable {
    private String entity;
    private boolean plain;
    private String menuId;
    private String text;
    private String iconCls;
    private String handler;
    private String orderNo;
    private Integer status;
}
