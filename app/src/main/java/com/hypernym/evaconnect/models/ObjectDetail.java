package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("is_url")
    @Expose
    private Boolean isUrl;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("post_image")
    @Expose
    private List<Object> postImage = null;
    @SerializedName("post_video")
    @Expose
    private Object postVideo;
    @SerializedName("created_by_id")
    @Expose
    private Integer createdById;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("modified_by_id")
    @Expose
    private Object modifiedById;
    @SerializedName("modified_datetime")
    @Expose
    private Object modifiedDatetime;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsUrl() {
        return isUrl;
    }

    public void setIsUrl(Boolean isUrl) {
        this.isUrl = isUrl;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Object> getPostImage() {
        return postImage;
    }

    public void setPostImage(List<Object> postImage) {
        this.postImage = postImage;
    }

    public Object getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(Object postVideo) {
        this.postVideo = postVideo;
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

    public Object getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Object modifiedById) {
        this.modifiedById = modifiedById;
    }

    public Object getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Object modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
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

}