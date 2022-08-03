package com.hypernym.evaconnect.models;

public class NnotificationModel {
    private String status;
    private String created_datetime;
    private String os;
    private Integer user_id;
    private Integer created_by;
    private NotificationDataClass notifications;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Integer created_by) {
        this.created_by = created_by;
    }

    public NotificationDataClass getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationDataClass notifications) {
        this.notifications = notifications;
    }
}
