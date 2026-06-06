package com.warpedcitadel.appusermanagement.audit.model;

public class UserSessionsModel {

    private String lastActiveDtm;

    public UserSessionsModel(String lastActiveDtm) {
        this.lastActiveDtm = lastActiveDtm;
    }

    public String getLastActiveDtm() {
        return lastActiveDtm;
    }
}
