package com.imau.brms.dto;

import lombok.Data;

@Data
public class ResourseDTO {
    private Integer resId;
    private Integer bookId;
    private String resName;
    private String resSrc;
    private String resType;
    private Integer downSum;
    private String bookName;
}
