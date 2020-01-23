package com.hypernym.evaconnect.models;

import java.io.Serializable;

public class Connection implements Serializable{
    private int receiver_id;
    private int sender_id;
    private String status;
    private Integer id;
    private Integer modified_by_id;
    private String modified_datetime;

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(Integer modified_by_id) {
        this.modified_by_id = modified_by_id;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }
}
