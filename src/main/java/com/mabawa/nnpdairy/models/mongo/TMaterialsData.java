package com.mabawa.nnpdairy.models.mongo;

import org.bson.types.Binary;

public class TMaterialsData {
    private Binary content;

    private Integer type;

    private String contentDownload;

    public Binary getContent() {
        return content;
    }
    public void setContent(Binary content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public String getContentDownload() {
        return contentDownload;
    }
    public void setContentDownload(String contentDownload) {
        this.contentDownload = contentDownload;
    }
}
