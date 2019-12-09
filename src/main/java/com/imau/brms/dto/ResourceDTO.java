package com.imau.brms.dto;

import lombok.Data;

@Data
public class ResourceDTO {
    private Integer resId;
    private Integer bookId;
    private String resName;
    private String resSrc;
    private String resType;
    private String resSize;
    private Integer downSum;
    private String bookName;
}
