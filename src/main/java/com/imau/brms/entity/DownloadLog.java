package com.imau.brms.entity;
import	java.util.Date;

import lombok.Data;

@Data
public class DownloadLog {
    private Long readerId;
    private String ip;
    private Integer bookId;
    private String bookName;
    private String resName;
    private Date downloadTime;
}
