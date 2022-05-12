package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

/*
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
*/


@Generated("jsonschema2pojo")
public class ShareConnection {

    @SerializedName("share_user_id")
    @Expose
    private List<Integer> shareUserId = null;
    @SerializedName("object_id")
    @Expose
    private Integer objectId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("news_id")
    @Expose
    private Integer newsId;


    @SerializedName("post_id")
    @Expose
    private Integer postId;


    @SerializedName("event_id")
    @Expose
    private Integer eventId;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @SerializedName("job_id")
    @Expose
    private Integer jobId;


    @SerializedName("object_type")
    @Expose
    private String objectType;

    public ShareConnection(Integer id, List<Integer> share_users) {
        this.userId = id;
        this.shareUserId = share_users;
    }

    public List<Integer> getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(List<Integer> shareUserId) {
        this.shareUserId = shareUserId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

}