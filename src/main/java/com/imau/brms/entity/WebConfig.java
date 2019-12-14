package com.imau.brms.entity;

import lombok.Data;

@Data
public class WebConfig {
    private String webName;
    private String title;
    private byte[] logoImg;
    private byte[] bgImg;
    private String copyright;
    private String icp;
    private String file1Name;
    private String file2Name;
    private String file3Name;


    public WebConfig() {
        this.webName = null;
        this.title = null;
        this.logoImg = null;
        this.bgImg = null;
        this.copyright = null;
        this.file1Name = null;
        this.file2Name = null;
        this.file3Name = null;
    }
}
