package com.hypernym.evaconnect.models;

import java.io.Serializable;
import java.util.List;

import okhttp3.MultipartBody;

public class Dashboard  implements Serializable {
    private String content;
    private List<MultipartBody.Part> attachments;
    private Integer created_by_id;
    private List<String> post_image;
    private Integer comment_count;
    private Integer like_count;
    private String created_datetime;
    private User user;
    private Integer id;
    private Integer object_id;
    private String status;
    private Integer post_type;
    private Integer post_id;
    private Integer is_post_like;
    private Integer total_connection;
    private String action;
    private String type;
    private String is_connected;
    private MultipartBody.Part video;
    private String post_video;
    private String link_thumbnail;
    private boolean is_receiver;
    private String object_type;
    private String details;
    private int is_like;
    private boolean is_url;
    private String job_title;
    private String job_nature;
    private String job_sector;
    private String position;
    private String weekly_hours;
    private String location;
    private Double salary;
    private String job_image;
    private Integer applicant_count;
    private Integer is_job_like;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MultipartBody.Part> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MultipartBody.Part> attachments) {
        this.attachments = attachments;
    }

    public Integer getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(Integer created_by_id) {
        this.created_by_id = created_by_id;
    }

    public List<String> getPost_image() {
        return post_image;
    }

    public void setPost_image(List<String> post_image) {
        this.post_image = post_image;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObject_id() {
        return object_id;
    }

    public void setObject_id(Integer object_id) {
        this.object_id = object_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPost_type() {
        return post_type;
    }

    public void setPost_type(Integer post_type) {
        this.post_type = post_type;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getIs_post_like() {
        return is_post_like;
    }

    public void setIs_post_like(Integer is_post_like) {
        this.is_post_like = is_post_like;
    }

    public Integer getTotal_connection() {
        return total_connection;
    }

    public void setTotal_connection(Integer total_connection) {
        this.total_connection = total_connection;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_connected() {
        return is_connected;
    }

    public void setIs_connected(String is_connected) {
        this.is_connected = is_connected;
    }

    public MultipartBody.Part getVideo() {
        return video;
    }

    public void setVideo(MultipartBody.Part video) {
        this.video = video;
    }

    public String getPost_video() {
        return post_video;
    }

    public void setPost_video(String post_video) {
        this.post_video = post_video;
    }

    public String getLink_thumbnail() {
        return link_thumbnail;
    }

    public void setLink_thumbnail(String link_thumbnail) {
        this.link_thumbnail = link_thumbnail;
    }

    public boolean isIs_receiver() {
        return is_receiver;
    }

    public void setIs_receiver(boolean is_receiver) {
        this.is_receiver = is_receiver;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public boolean isIs_url() {
        return is_url;
    }

    public void setIs_url(boolean is_url) {
        this.is_url = is_url;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_nature() {
        return job_nature;
    }

    public void setJob_nature(String job_nature) {
        this.job_nature = job_nature;
    }

    public String getJob_sector() {
        return job_sector;
    }

    public void setJob_sector(String job_sector) {
        this.job_sector = job_sector;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWeekly_hours() {
        return weekly_hours;
    }

    public void setWeekly_hours(String weekly_hours) {
        this.weekly_hours = weekly_hours;
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

    public String getJob_image() {
        return job_image;
    }

    public void setJob_image(String job_image) {
        this.job_image = job_image;
    }

    public Integer getApplicant_count() {
        return applicant_count;
    }

    public void setApplicant_count(Integer applicant_count) {
        this.applicant_count = applicant_count;
    }

    public Integer getIs_job_like() {
        return is_job_like;
    }

    public void setIs_job_like(Integer is_job_like) {
        this.is_job_like = is_job_like;
    }
}
