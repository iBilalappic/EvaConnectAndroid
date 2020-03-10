package com.hypernym.evaconnect.models;

import java.io.Serializable;
import java.util.List;

public class ChatMessage {
    private String Message;
    private String Type_interview;
    private String message_key;
    private String Job_id;
    private String Day;
    private String Month;
    private String Year;
    private String Minutes;
    private String Hour;

    public String getSender_id() {
        return Sender_id;
    }

    public void setSender_id(String sender_id) {
        Sender_id = sender_id;
    }

    private String Sender_id;

    public String getApplication_id() {
        return Application_id;
    }

    public void setApplication_id(String application_id) {
        Application_id = application_id;
    }

    private String Application_id;


    public String getJob_id() {
        return Job_id;
    }

    public void setJob_id(String job_id) {
        Job_id = job_id;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }


    public String getMinutes() {
        return Minutes;
    }

    public void setMinutes(String minutes) {
        Minutes = minutes;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getType_interview() {
        return Type_interview;
    }

    public void setType_interview(String type_interview) {
        Type_interview = type_interview;
    }

    private int type;
    private List<String> image;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getChattime() {
        return chattime;
    }

    public void setChattime(String chattime) {
        this.chattime = chattime;
    }

    private String chattime;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getMessage_key() {
        return message_key;
    }

    public void setMessage_key(String message_key) {
        this.message_key = message_key;
    }
}
