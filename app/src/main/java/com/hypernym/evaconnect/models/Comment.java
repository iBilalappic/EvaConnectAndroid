package com.hypernym.evaconnect.models;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.Serializable;

public class Comment implements Serializable {
    private Integer post_id;
    private String content;
    private Integer created_by_id;
    private String status;
    private String created_datetime;
    private User user;



    public String getContent() {
        String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(content);

        return fromServerUnicodeDecoded;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(Integer created_by_id) {
        this.created_by_id = created_by_id;
    }
}
