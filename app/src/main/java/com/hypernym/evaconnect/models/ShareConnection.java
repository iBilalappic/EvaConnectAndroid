package com.hypernym.evaconnect.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShareConnection {
    public ShareConnection( int user_id, List<Integer> share_user_id) {

        this.user_id = user_id;
        this.share_user_id = share_user_id;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    @SerializedName("job_id")
    private int job_id;

    @SerializedName("object_id")
    private int object_id;

    @SerializedName("object_type")
    private String object_type;

    @SerializedName("post_id")
    private int post_id;


    @SerializedName("event_id")
    private int event_id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("share_user_id")
    private List<Integer> share_user_id;
}
