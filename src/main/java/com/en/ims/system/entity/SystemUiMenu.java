package com.en.ims.system.entity;

import com.en.ims.common.entity.BaseTable;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="system_ui_menu")
public class SystemUiMenu extends BaseTable {
    private String superId;
    private String text;
    private String iconCls;
    private String entity;
    private Integer status;
    private Integer moduleId;
    private Integer orderNo;
    @Transient
    private List<SystemUiMenu> children;
}
