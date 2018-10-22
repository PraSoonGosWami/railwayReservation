package com.invaderx.railway.models;

public class UserProfile {
    private String uid;
    private String phone;
    private int wallet;
    private String upi;
    private String card;
    private String paytm;
    private String home;

    public UserProfile(){

    }

    public UserProfile(String uid, String phone, int wallet, String upi, String card, String paytm, String home) {
        this.uid = uid;
        this.phone = phone;
        this.wallet = wallet;
        this.upi = upi;
        this.card = card;
        this.paytm = paytm;
        this.home = home;
    }

    public String getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }


    public int getWallet() {
        return wallet;
    }

    public String getUpi() {
        return upi;
    }

    public String getCard() {
        return card;
    }

    public String getPaytm() {
        return paytm;
    }

    public String getHome() {
        return home;
    }
}
