package com.warpedcitadel.appusermanagement.auth.model;

public class EmailVerificationModel {

    private long    appUserId;
    private String  token;
    private String  passcode;
    private boolean isUsed;


    public EmailVerificationModel() {

    }

    public EmailVerificationModel(long appUserId, String token, String passcode, boolean isUsed) {
        this.appUserId = appUserId;
        this.token = token;
        this.passcode = passcode;
        this.isUsed = isUsed;
    }

    public long getAppUserId() {
        return appUserId;
    }

    public String getToken() {
        return token;
    }

    public String getPasscode() {
        return passcode;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
