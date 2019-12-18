package com.imau.brms.entity;

import lombok.Data;

@Data
public class ReaderInfo {
    private Long readerId;
    private String name;
    private String department;
    private String major;
    private String grade;
    private String className;
}
