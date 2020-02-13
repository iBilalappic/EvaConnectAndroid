package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyLikesModel  {

    @SerializedName("object_id")
    @Expose
    private Integer objectId;
    @SerializedName("object_type")
    @Expose
    private String objectType;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("created_by_id")
    @Expose
    private Integer createdById;
    @SerializedName("user")
    @Expose
    private List<UserModel> user = null;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("is_like")
    @Expose
    private Integer isLike;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public List<UserModel> getUser() {
        return user;
    }

    public void setUser(List<UserModel> user) {
        this.user = user;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getIsLike() {
        return isLike;
    }

    public void setIsLike(Integer isLike) {
        this.isLike = isLike;
    }

}