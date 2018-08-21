package com.invaderx.railway.pojoClasses;

import java.util.List;

public class Ticket {
    private String ticketNameNumber;
    private String src;
    private String dest;
    private String travelClass;
    private String time;
    private String fare;
    private String date;
    private List<Passengers> people;

    public Ticket(String ticketNameNumber, String src, String dest, String travelClass, String time, String fare, String date,List<Passengers> people) {
        this.ticketNameNumber = ticketNameNumber;
        this.src = src;
        this.dest = dest;
        this.travelClass = travelClass;
        this.time = time;
        this.fare = fare;
        this.date = date;
        this.people=people;
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

    public List<Passengers> getPeople() {
        return people;
    }
}
