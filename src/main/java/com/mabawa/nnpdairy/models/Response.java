package com.mabawa.nnpdairy.models;

import java.util.Map;

public class Response {
    private String title;
    private String message;
    private String status;
    private int success;
    private Map data;

    public Response() {
    }

    public Response(String title, String message, String status, int success, Map data) {
        this.title = title;
        this.message = message;
        this.status = status;
        this.success = success;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getSuccess() {
        return success;
    }
    public void setSuccess(int success) {
        this.success = success;
    }

    public Map getData() {
        return data;
    }
    public void setData(Map data) {
        this.data = data;
    }
}
