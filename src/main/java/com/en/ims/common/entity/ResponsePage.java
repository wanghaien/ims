package com.en.ims.common.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class ResponsePage<T> {
    private List<T> rows;
    private long total;
}
