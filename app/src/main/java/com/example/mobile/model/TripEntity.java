package com.example.mobile.model;

import com.example.mobile.Constant;

import java.util.Calendar;
import java.util.Date;

public class TripEntity {
    private int id;
    private String name;
    private String destination;
    private String transportation;
    private int participant;
    private Date date;
    private int risk;
    private String description;

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int isRisk() {
        return risk;
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TripEntity(int id, String name, String destination, String transportation, int participant, Date date, int risk, String description) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.transportation = transportation;
        this.participant = participant;
        this.date = date;
        this.risk = risk;
        this.description = description;
    }

    public TripEntity() {
        this.setTransportation(Constant.EMPTY_STRING);
        this.setParticipant(Constant.NEW_ENTITY_ID);
        this.setId(Constant.NEW_ENTITY_ID);
        this.setName(Constant.EMPTY_STRING);
        this.setDate(Calendar.getInstance().getTime());
        this.setRisk(0);
        this.setDescription(Constant.EMPTY_STRING);
    }
}
