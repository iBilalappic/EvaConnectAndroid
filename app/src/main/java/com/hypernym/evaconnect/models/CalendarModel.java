package com.hypernym.evaconnect.models;

import com.google.gson.annotations.SerializedName;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;
import java.util.List;

public class CalendarModel implements Serializable {
    private int user_id;
    private String month;
    private String year;
    private String object_type;
    private String occurrence_date;
    private String date;
    private String notes;

    private String status;
    private String object_id;

    @SerializedName("object_details")
    private Post object_details;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getOccurrence_date() {
        return occurrence_date;
    }

    public void setOccurrence_date(String occurrence_date) {
        this.occurrence_date = occurrence_date;
    }

     public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public Post getObject_details() {
        return object_details;
    }

    public void setObject_details(Post object_details) {
        this.object_details = object_details;
    }
}
