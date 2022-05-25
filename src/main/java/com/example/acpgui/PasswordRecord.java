package com.example.acpgui;

import javafx.beans.property.SimpleStringProperty;

public class PasswordRecord {
    private final SimpleStringProperty siteAndApp;
    private final SimpleStringProperty accountUserName;

    public PasswordRecord(String siteAndApp, String accountUserName) {
        this.siteAndApp = new SimpleStringProperty(siteAndApp);
        this.accountUserName = new SimpleStringProperty(accountUserName);
    }

    public String getSiteAndApp() {
        return siteAndApp.get();
    }


    public String getAccountUserName() {
        return accountUserName.get();
    }

}




