package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AppliedApplicants implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("user")
    @Expose
    private User_applicants user;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("content")
    @Expose
    private String content;
    private String application_attachment;
    @SerializedName("created_by_id")
    @Expose
    private Integer createdById;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("status")
    @Expose
    private String status;

    private Integer modified_by_id;

    private String modified_datetime;

    private String message_key;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public User_applicants getUser() {
        return user;
    }

    public void setUser(User_applicants user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApplicationAttachment() {
        return application_attachment;
    }

    public void setApplicationAttachment(String applicationAttachment) {
        this.application_attachment = applicationAttachment;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage_key() {
        return message_key;
    }

    public void setMessage_key(String message_key) {
        this.message_key = message_key;
    }



    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public Integer getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(Integer modified_by_id) {
        this.modified_by_id = modified_by_id;
    }
}