package com.hypernym.evaconnect.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateMeeting {

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("created_by_id")
    private int created_by_id;

    @SerializedName("subject")
    private String subject;

    @SerializedName("notes")
    private String notes;

    @SerializedName("address")
    private String address;

    @SerializedName("start_date")
    private String start_date;

    @SerializedName("end_date")
    private String end_date;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_time;

    @SerializedName("status")
    private String status;

    @SerializedName("meeting_attendees")
    private List<Integer> event_attendees;

    public CreateMeeting(int user_id, int created_by_id, String subject, String notes, String address, String start_date, String end_date, String start_time, String end_time, String status, List<Integer> event_attendees) {
        this.user_id = user_id;
        this.created_by_id = created_by_id;
        this.subject = subject;
        this.notes = notes;
        this.address = address;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.status = status;
        this.event_attendees = event_attendees;
    }
}
