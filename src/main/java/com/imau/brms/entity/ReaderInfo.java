package com.imau.brms.entity;
import	java.util.Date;

import lombok.Data;

@Data
public class ReaderInfo {
    private Long readerId;
    private String name;
    private String sex;
    private Date birth;
    private String address;
    private String telcode;
}
