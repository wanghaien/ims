package com.en.ims.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class BaseTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date modifyTime;
    private Integer status;
}
