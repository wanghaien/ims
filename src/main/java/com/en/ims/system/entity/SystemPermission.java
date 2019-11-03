package com.en.ims.system.entity;

import com.en.ims.common.entity.BaseTable;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="system_permission")
public class SystemPermission extends BaseTable {
    private String permissionname;
    private String url;

}
