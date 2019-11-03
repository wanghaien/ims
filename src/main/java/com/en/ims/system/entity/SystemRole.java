package com.en.ims.system.entity;

import com.en.ims.common.entity.BaseTable;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="system_role")
public class SystemRole extends BaseTable {
    private String rolename;
}