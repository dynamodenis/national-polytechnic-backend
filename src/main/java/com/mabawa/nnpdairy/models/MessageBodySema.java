package com.mabawa.nnpdairy.models;

public class MessageBodySema {
    private String text;
    private String recipients;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getRecipients() {
        return recipients;
    }
    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }
}
