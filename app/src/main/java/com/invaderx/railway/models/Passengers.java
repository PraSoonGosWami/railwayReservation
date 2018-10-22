package com.invaderx.railway.models;

public class Passengers {
    private String pName;
    private int pAge;
    private String pSex;

    public Passengers(){}

    public Passengers(String pName, int pAge, String pSex) {
        this.pName = pName;
        this.pAge = pAge;
        this.pSex = pSex;
    }

    public String getpName() {
        return pName;
    }

    public int getpAge() {
        return pAge;
    }

    public String getpSex() {
        return pSex;
    }
}
