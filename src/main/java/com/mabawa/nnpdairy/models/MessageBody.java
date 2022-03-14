package com.mabawa.nnpdairy.models;

public class MessageBody {
    private String msisdn;
    private String sms;
    private String productID;
    private String callbackURL;

    public String getMsisdn() {
        return msisdn;
    }
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSms() {
        return sms;
    }
    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getProductID() {
        return productID;
    }
    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCallbackURL() {
        return callbackURL;
    }
    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }
}
