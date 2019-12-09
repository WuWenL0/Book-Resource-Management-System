package com.imau.brms.entity;

import lombok.Data;

@Data
public class Resource {
    private Integer resId;
    private Integer bookId;
    private String resName;
    private String resSrc;
    private String resType;
    private Long resSize;
    private Integer downSum;
}
