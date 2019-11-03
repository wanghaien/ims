package com.en.ims.system.entity;

import com.en.ims.common.entity.BaseTable;
import lombok.Data;

import javax.persistence.*;

/**
 * 系统模块
 */
@Entity
@Data
@Table(name="system_ui_module")
public class SystemUiModule extends BaseTable {
    private String text;
    private String iconCls;
    private Integer status;
    private Integer orderNo;
}
