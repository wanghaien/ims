package com.en.ims.system.entity;

import com.en.ims.common.entity.BaseTable;
import lombok.Data;

import javax.persistence.*;

/**
 * 界面主表
 */
@Entity
@Data
@Table(name="system_ui_table")
public class SystemUiTable extends BaseTable {
    private String align;
    private String entity;
    private String menuId;
    private String field;
    private String title;
    private String width;
    private boolean sortable;
    private Integer orderNo;
    private boolean checkbox;
}
