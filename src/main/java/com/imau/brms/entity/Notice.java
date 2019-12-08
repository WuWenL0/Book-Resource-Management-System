package com.imau.brms.entity;
import	java.util.Date;

import lombok.Data;

@Data
public class Notice {
    private Integer id;
    private String name;
    private String context;
    private Date creatTime;
}
