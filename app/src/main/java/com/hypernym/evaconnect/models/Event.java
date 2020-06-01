package com.hypernym.evaconnect.models;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {
    private Integer event_id;
    private String event_name;
    private String event_city;
    private String event_address;
    private String start_time;
    private String end_time;
    private String event_start_date;
    private String event_end_date;
    private List<String> event_image;
    private String content;
    private String created_datetime;
    private Integer user_id;
    private String is_attending;
    private Integer is_event_like;
    private int like_count;
    private int comment_count;
    private int going;
    private int not_going;
    private int may_be;
    private String status;
    private String attendance_status;
    private String attending_date;
    private String attending;
    private String modified_datetime;
    private int modified_by_id;
    private int created_by_id;
    private String action;
    private List<Integer> event_attendeeIDs;
    private String registration_link;
    private int is_private;
    private List<EventAttendees> event_attendees;

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_city() {
        return event_city;
    }

    public void setEvent_city(String event_city) {
        this.event_city = event_city;
    }

    public String getEvent_address() {
        return event_address;
    }

    public void setEvent_address(String event_address) {
        this.event_address = event_address;
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

    public String getEvent_start_date() {
        return event_start_date;
    }

    public void setEvent_start_date(String event_start_date) {
        this.event_start_date = event_start_date;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public void setEvent_end_date(String event_end_date) {
        this.event_end_date = event_end_date;
    }

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

    public List<String> getEvent_image() {
        return event_image;
    }

    public void setEvent_image(List<String> event_image) {
        this.event_image = event_image;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setIs_attending(String is_attending) {
        this.is_attending = is_attending;
    }

    public String getIs_attending() {
        return is_attending;
    }

    public Integer getIs_event_like() {
        return is_event_like;
    }

    public void setIs_event_like(Integer is_event_like) {
        this.is_event_like = is_event_like;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getGoing() {
        return going;
    }

    public void setGoing(int going) {
        this.going = going;
    }

    public int getNot_going() {
        return not_going;
    }

    public void setNot_going(int not_going) {
        this.not_going = not_going;
    }

    public int getMay_be() {
        return may_be;
    }

    public void setMay_be(int may_be) {
        this.may_be = may_be;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttendance_status() {
        return attendance_status;
    }

    public void setAttendance_status(String attendance_status) {
        this.attendance_status = attendance_status;
    }

    public String getAttending_date() {
        return attending_date;
    }

    public void setAttending_date(String attending_date) {
        this.attending_date = attending_date;
    }

    public String getAttending() {
        return attending;
    }

    public void setAttending(String attending) {
        this.attending = attending;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public int getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(int modified_by_id) {
        this.modified_by_id = modified_by_id;
    }

    public int getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(int created_by_id) {
        this.created_by_id = created_by_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Integer> getEvent_attendeeIDs() {
        return event_attendeeIDs;
    }

    public void setEvent_attendeeIDs(List<Integer> event_attendeeIDs) {
        this.event_attendeeIDs = event_attendeeIDs;
    }

//    public List<InviteConnections> getEvent_attendees() {
//        return event_attendees;
//    }
//
//    public void setEvent_attendees(List<InviteConnections> event_attendees) {
//        this.event_attendees = event_attendees;
//    }

    public String getRegistration_link() {
        return registration_link;
    }

    public void setRegistration_link(String registration_link) {
        this.registration_link = registration_link;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public List<EventAttendees> getEvent_attendees() {
        return event_attendees;
    }

    public void setEvent_attendees(List<EventAttendees> event_attendees) {
        this.event_attendees = event_attendees;
    }
}
