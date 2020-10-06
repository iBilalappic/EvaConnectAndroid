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
    private Integer event_id;
    private Integer rss_news_id;
    private Integer modified_by_id;
    private String modified_datetime;
    private Integer id;
    private boolean isPostMine;



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

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public Integer getRss_news_id() {
        return rss_news_id;
    }

    public void setRss_news_id(Integer rss_news_id) {
        this.rss_news_id = rss_news_id;
    }

    public Integer getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(Integer modified_by_id) {
        this.modified_by_id = modified_by_id;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isPostMine() {
        return isPostMine;
    }

    public void setPostMine(boolean postMine) {
        isPostMine = postMine;
    }
}
