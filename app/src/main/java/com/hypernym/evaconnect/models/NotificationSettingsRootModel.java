package com.hypernym.evaconnect.models;

import com.google.gson.annotations.SerializedName;

public class NotificationSettingsRootModel {

    public String status;
    public Integer created_by;
    public String created_datetime;
    public Integer user_id;
    public String os;

    @SerializedName("notifications")
    public NotificationSettingsModel notificationSettingsModel;

    public NotificationSettingsRootModel(String status, Integer created_by, String created_datetime, Integer user_id, String os, NotificationSettingsModel notificationSettingsModel) {
        this.status = status;
        this.created_by = created_by;
        this.created_datetime = created_datetime;
        this.user_id = user_id;
        this.os = os;
        this.notificationSettingsModel = notificationSettingsModel;
    }
}

