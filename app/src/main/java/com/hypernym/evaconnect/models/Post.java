package com.hypernym.evaconnect.models;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import okhttp3.MultipartBody;

public class Post implements Serializable {
    private String content;
    private List<MultipartBody.Part> attachments;
    private Integer created_by_id;
    private List<String> post_image;
    private Integer comment_count;
    private Integer like_count;
    private String created_datetime;
    private User user;
    private Integer id;
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



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public List<MultipartBody.Part> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MultipartBody.Part> attachments) {
        this.attachments = attachments;
    }

    public List<String> getPost_image() {
        return post_image;
    }

    public void setPost_image(List<String> post_image) {
        this.post_image = post_image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotal_connection() {
        return total_connection;
    }

    public void setTotal_connection(Integer total_connection) {
        this.total_connection = total_connection;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(Integer created_by_id) {
        this.created_by_id = created_by_id;
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
}

