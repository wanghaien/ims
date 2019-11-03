package com.en.ims.common.entity;

import lombok.Data;

@Data
public class RequestPage {
    private Integer page;
    private Integer rows;
    private String sort;
    private String order;
    private Integer total;
}
