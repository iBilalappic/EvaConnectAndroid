package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveEventData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("event_id")
    @Expose
    private Integer event_id;
    @SerializedName("created_by_id")
    @Expose
    private Object createdById;
    @SerializedName("os")
    @Expose
    private Object os;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("is_favourite")
    @Expose
    private Boolean isFavourite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getevent_id() {
        return event_id;
    }

    public void setevent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public Object getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Object createdById) {
        this.createdById = createdById;
    }

    public Object getOs() {
        return os;
    }

    public void setOs(Object os) {
        this.os = os;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        this.isFavourite = isFavourite;
    }


}
