package com.hypernym.evaconnect.models;

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
    private Integer object_id;
    private Integer is_applied;
    private String name;
    private News news_source;
    private String title;
    private String link;
    private int rss_news_id;
    private Integer receiver_id;
    private int modified_by_id;

    public String getIs_attending() {
        return is_attending;
    }

    public void setIs_attending(String is_attending) {
        this.is_attending = is_attending;
    }

    private String is_attending;
    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    private int share_count;
    private String modified_datetime;
    private String post_document;

    public String getOccurrence_date() {
        return occurrence_date;
    }

    public void setOccurrence_date(String occurrence_date) {
        this.occurrence_date = occurrence_date;
    }

    private String occurrence_date;

    public String getOccurrence_time() {
        return occurrence_time;
    }

    public void setOccurrence_time(String occurrence_time) {
        this.occurrence_time = occurrence_time;
    }

    private String occurrence_time;


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private String notes;



    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    private String start_date;
    private String end_date;
    private String company_name;
    private String interview_time;
    private Integer connection_id;


    public Integer getObject_id() {
        return object_id;
    }

    public void setObject_id(Integer object_id) {
        this.object_id = object_id;
    }

    private String status;

    private Integer post_type;
    private Integer post_id;
    private Integer is_post_like;
    private Integer is_news_like;
    private Integer total_connection;
    private String action;
    private String type;

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    private String job_type;

    public int getActive_hours() {
        return active_hours;
    }

    public void setActive_hours(int active_hours) {
        this.active_hours = active_hours;
    }

    private int active_hours;
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

    public Integer getAttendees() {
        return attendees;
    }

    private Integer attendees;
    private Integer is_job_like;
    private List<String> event_image;

    private String event_city;
    private Integer event_id;
    private Integer is_event_like;
    private String start_time;
    private String end_time;
    private String address;
    private int is_private;
    private String image;

    private MultipartBody.Part document;



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

    public Integer getIs_applied() {
        return is_applied;
    }

    public void setIs_applied(Integer is_applied) {
        this.is_applied = is_applied;
    }

    public List<String> getEvent_image() {
        return event_image;
    }

    public void setEvent_image(List<String> event_image) {
        this.event_image = event_image;
    }

    public String getEvent_city() {
        return event_city;
    }

    public void setEvent_city(String event_city) {
        this.event_city = event_city;
    }


    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public Integer getIs_event_like() {
        return is_event_like;
    }

    public void setIs_event_like(Integer is_event_like) {
        this.is_event_like = is_event_like;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getInterview_time() {
        return interview_time;
    }

    public void setInterview_time(String interview_time) {
        this.interview_time = interview_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public Integer getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(Integer connection_id) {
        this.connection_id = connection_id;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public News getNews_source() {
        return news_source;
    }

    public void setNews_source(News news_source) {
        this.news_source = news_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRss_news_id() {
        return rss_news_id;
    }

    public void setRss_news_id(int rss_news_id) {
        this.rss_news_id = rss_news_id;
    }

    public Integer getIs_news_like() {
        return is_news_like;
    }

    public void setIs_news_like(Integer is_news_like) {
        this.is_news_like = is_news_like;
    }

    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public MultipartBody.Part getDocument() {
        return document;
    }

    public void setDocument(MultipartBody.Part document) {
        this.document = document;
    }

    public int getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(int modified_by_id) {
        this.modified_by_id = modified_by_id;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public String getPost_document() {
        return post_document;
    }

    public void setPost_document(String post_document) {
        this.post_document = post_document;
    }
}

