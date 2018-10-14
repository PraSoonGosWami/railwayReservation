package com.invaderx.railway.models;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private String ticketNameNumber;
    private String src;
    private String dest;
    private String travelClass;
    private String time;
    private String fare;
    private String date;
    private ArrayList<Passengers> people;
    private String seatNo;
    private String pnr;
    private String trainNo;
    private String baseClass;

    public Ticket(){

    }
    public Ticket(String ticketNameNumber, String src, String dest, String travelClass, String time, String fare, String date,ArrayList<Passengers> people,
                  String seatNo,String pnr,String trainNo,String baseClass) {
        this.ticketNameNumber = ticketNameNumber;
        this.src = src;
        this.dest = dest;
        this.travelClass = travelClass;
        this.time = time;
        this.fare = fare;
        this.date = date;
        this.people=people;
        this.seatNo=seatNo;
        this.pnr=pnr;
        this.trainNo = trainNo;
        this.baseClass = baseClass;
    }

    public String getTicketNameNumber() {
        return ticketNameNumber;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public String getTime() {
        return time;
    }

    public String getFare() {
        return fare;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Passengers> getPeople() {
        return people;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getPnr() {
        return pnr;
    }

    public String getTrainNo(){ return trainNo;}

    public String getBaseClass() {
        return baseClass;
    }
}
