package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotesData {

    @SerializedName("id")
    @Expose
   public Integer id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("details")
    @Expose
    public String details;
    @SerializedName("occurrence_date")
    @Expose
    public  String occurrenceDate;
    @SerializedName("occurrence_time")
    @Expose
    public  String occurrenceTime;
    @SerializedName("created_datetime")
    @Expose
    public   String createdDatetime;
    @SerializedName("is_notify")
    @Expose
    public  Boolean isNotify;
    @SerializedName("user_id")
    @Expose
    public    Integer userId;
    @SerializedName("os")
    @Expose
    public   String os;
    @SerializedName("status")
    @Expose
    public   Integer status;
    @SerializedName("object_id")
    @Expose
    public   Integer objectId;
    @SerializedName("object_type")
    @Expose
    public   String objectType;
}
