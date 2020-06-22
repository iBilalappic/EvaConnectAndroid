package com.hypernym.evaconnect.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meeting {

    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("created_by_id")
    private int created_by_id;

    @SerializedName("name")
    private String name;

    @SerializedName("content")
    private String content;

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

    @SerializedName("attendees")
    private List<Integer> attendees;

    private int modified_by_id;
    private String modified_datetime;

    private int meeting_id;
    private String attendance_status;




    public int getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(int modified_by_id) {
        this.modified_by_id = modified_by_id;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(int created_by_id) {
        this.created_by_id = created_by_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Integer> attendees) {
        this.attendees = attendees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(int meeting_id) {
        this.meeting_id = meeting_id;
    }

    public String getAttendance_status() {
        return attendance_status;
    }

    public void setAttendance_status(String attendance_status) {
        this.attendance_status = attendance_status;
    }
}
