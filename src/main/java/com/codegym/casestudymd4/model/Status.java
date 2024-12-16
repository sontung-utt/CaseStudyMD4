package com.codegym.casestudymd4.model;

public enum Status {
    PENDING("Pending"),
    DELIVERY("Delivery"),
    SUCCESS("Success");

    Status() {
    }
    private String displayName;
    Status(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

}
