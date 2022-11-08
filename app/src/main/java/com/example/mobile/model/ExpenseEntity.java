package com.example.mobile.model;

import com.example.mobile.Constant;

import java.util.Calendar;
import java.util.Date;

public class ExpenseEntity {
    private int id;
    private String type;
    private float amount;
    private Date time;
    private String comment;
    private int tripID;

    public int getTripID() {
        return tripID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ExpenseEntity(int id, String type, float amount, Date time, String comment, int tripID) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.comment = comment;
        this.tripID = tripID;
    }

    public ExpenseEntity() {
        this.setId(Constant.NEW_ENTITY_ID);
        this.setType(Constant.EMPTY_STRING);
        this.setAmount(Constant.EMPTY_FLOAT);
        this.setTime(Calendar.getInstance().getTime());
        this.setComment(Constant.EMPTY_STRING);
    }
}
