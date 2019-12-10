package com.imau.brms.entity;
import	java.util.Date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ReaderInfo {
    private Long readerId;
    private String name;
    private String sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private String address;
    private String telcode;
}
