package com.imau.brms.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Book {
    private Integer bookId;
    private String name;
    private String author;
    private String publish;
    private String pubdate;
    private String introduction;
    private String isbn;
    private String classId;
    private String pressmark;
    private Date updateTime;

}
