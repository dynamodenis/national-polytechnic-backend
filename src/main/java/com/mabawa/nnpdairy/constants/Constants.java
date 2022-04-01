package com.mabawa.nnpdairy.constants;

public class Constants {
    public static String[] TITLES = new String[]{"Results"};
    public static String[] STATUS = new String[]{"success", "info", "error"};
    public static String[] MESSAGES = new String[]{"Record Successfully Saved.", "Record Save failed.Check Errors!", "Logged In Successfully.", "Record(s) Were Found", "Record(s) deleted Successfully", "Record(s) not Found", "Active Session.", "Operation Successful.", "Update Successful"};

    public static String X_TOKEN = "030e7ff5c962457d8c315c9c4078bc92";
    public static String PRODUCT_ID = "15cab4599333463989e11552ebc61645";
    public static String SMS_SANDBOX_URL = "https://apis.sematime.com/v1/" + PRODUCT_ID + "/messages/single";

    public static String WAITING_FEEDBACK = "WAITING_FEEDBACK";
    public static String SENT = "SENT";
    public static String FAILED = "FAILED";
}
