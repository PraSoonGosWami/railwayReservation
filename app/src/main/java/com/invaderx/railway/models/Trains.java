package com.invaderx.railway.models;

import java.util.ArrayList;

public class Trains {
    private String class1A;
    private String class2A;
    private String class3A;
    private String classCC;
    private String classSL;
    private String time;
    private int dFri;
    private int dMon;
    private int dSat;
    private int dSun;
    private int dThur;
    private int dTue;
    private int dWed;
    private int seat1A;
    private int seat2A;
    private int seat3A;
    private int seatCC;
    private int seatSL;
    private ArrayList<String> stations;
    private String tName;
    private String tNumber;

    public Trains() {
    }

    public Trains(String time, String class1A, String class2A, String class3A, String classCC, String classSL, int dFri, int dMon, int dSat, int dSun, int dThur, int dTue, int dWed, int seat1A, int seat2A, int seat3A, int seatCC, int seatSL, ArrayList<String> stations, String tName, String tNumber) {
        this.class1A = class1A;
        this.time=time;
        this.class2A = class2A;
        this.class3A = class3A;
        this.classCC = classCC;
        this.classSL = classSL;
        this.dFri = dFri;
        this.dMon = dMon;
        this.dSat = dSat;
        this.dSun = dSun;
        this.dThur = dThur;
        this.dTue = dTue;
        this.dWed = dWed;
        this.seat1A = seat1A;
        this.seat2A = seat2A;
        this.seat3A = seat3A;
        this.seatCC = seatCC;
        this.seatSL = seatSL;
        this.stations = stations;
        this.tName = tName;
        this.tNumber = tNumber;
    }

    public String getClass1A() {
        return class1A;
    }

    public String getClass2A() {
        return class2A;
    }

    public String getClass3A() {
        return class3A;
    }

    public String getClassCC() {
        return classCC;
    }

    public String getClassSL() {
        return classSL;
    }

    public int getdFri() {
        return dFri;
    }

    public int getdMon() {
        return dMon;
    }

    public int getdSat() {
        return dSat;
    }

    public int getdSun() {
        return dSun;
    }

    public int getdThur() {
        return dThur;
    }

    public int getdTue() {
        return dTue;
    }

    public int getdWed() {
        return dWed;
    }

    public int getSeat1A() {
        return seat1A;
    }

    public int getSeat2A() {
        return seat2A;
    }

    public int getSeat3A() {
        return seat3A;
    }

    public int getSeatCC() {
        return seatCC;
    }

    public int getSeatSL() {
        return seatSL;
    }

    public ArrayList<String> getStations() {
        return stations;
    }

    public String gettName() {
        return tName;
    }

    public String gettNumber() {
        return tNumber;
    }
    public String getTime(){ return time;}
}
