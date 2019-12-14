package com.imau.brms.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Horse {
    private Integer id;
    private String name;
    private String author;
    private String grade;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;
    private String file1Src;
    private String file2Src;
    private String file3Src;
}
