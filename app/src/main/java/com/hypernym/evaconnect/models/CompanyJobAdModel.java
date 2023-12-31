package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompanyJobAdModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("job_nature")
    @Expose
    private String jobNature;
    @SerializedName("job_sector")
    @Expose
    private String jobSector;

    @SerializedName("job_type")
    @Expose
    private String jobType;

    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("weekly_hours")
    @Expose
    private String weeklyHours;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("salary")
    @Expose
    private Double salary;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("job_image")
    @Expose
    private String jobImage;
    @SerializedName("job_video")
    @Expose
    private Object jobVideo;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
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

    public Integer getActive_hours() {
        return active_hours;
    }

    public void setActive_hours(Integer active_hours) {
        this.active_hours = active_hours;
    }

    @Expose
    private Integer active_hours;
    @SerializedName("active_hours")

    public Integer getApplicant_count() {
        return applicant_count;
    }

    public void setApplicant_count(Integer applicant_count) {
        this.applicant_count = applicant_count;
    }

    @SerializedName("applicant_count")
    @Expose
    private Integer applicant_count;


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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobNature() {
        return jobNature;
    }

    public void setJobNature(String jobNature) {
        this.jobNature = jobNature;
    }

    public String getJobSector() {
        return jobSector;
    }

    public void setJobSector(String jobSector) {
        this.jobSector = jobSector;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(String weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getJobImage() {
        return jobImage;
    }

    public void setJobImage(String jobImage) {
        this.jobImage = jobImage;
    }

    public Object getJobVideo() {
        return jobVideo;
    }

    public void setJobVideo(Object jobVideo) {
        this.jobVideo = jobVideo;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
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

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}