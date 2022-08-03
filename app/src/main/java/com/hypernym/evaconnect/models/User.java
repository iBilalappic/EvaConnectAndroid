package com.hypernym.evaconnect.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@SuppressLint("ParcelCreator")
public class User implements Serializable, Parcelable {

    private Integer id;
    private String email;
    private String password;
    public String first_name;
    private String bio_data;
    private String type;
    private String status;
    private String username;
    private String token;
    private Integer user_id;
    private String user_image;
    private String linkedin_image_url;
    private String facebook_image_url;
    private String city;
    private String about;
    private String language;
    private String country;
    private String date_of_birth;
    private String last_name;
    private String sector;
    private String work_aviation;
    private String designation;
    private String field;
    private String address;
    private String company_name;
    private String filter;
    private String chatID;
    private boolean is_online;
    private String last_online_datetime;
    private String other_sector;
    private String company_url;
    private Integer modified_by_id;


    public String getSearch_key() {
        return search_key;
    }

    public void setSearch_key(String search_key) {
        this.search_key = search_key;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    private String search_key;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean isIs_shared() {
        return is_shared;
    }

    public void setIs_shared(boolean is_shared) {
        this.is_shared = is_shared;
    }

    private boolean is_shared=false;

    public String getConnection_status() {
        return connection_status;
    }

    public void setConnection_status(String connection_status) {
        this.connection_status = connection_status;
    }

    private String connection_status;
    private Integer total_connection;

    public Integer getIs_notifications() {
        return is_notifications;
    }

    public void setIs_notifications(Integer is_notifications) {
        this.is_notifications = is_notifications;
    }

    private Integer is_notifications;
    private String is_connected;

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    private String login_type;
    private boolean is_receiver;
    private Integer connection_id;
    private Integer receiver_id;
    private Integer is_linkedin;
    private Integer is_facebook;


    public Integer getConnection_count() {
        return connection_count;
    }

    public void setConnection_count(int connection_count) {
        this.connection_count = connection_count;
    }

    public int connection_count;


    public String getFacebook_image_url() {
        return facebook_image_url;
    }

    public void setFacebook_image_url(String facebook_image_url) {
        this.facebook_image_url = facebook_image_url;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getWork_aviation() {
        return work_aviation;
    }

    public void setWork_aviation(String work_aviation) {
        this.work_aviation = work_aviation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getLinkedin_image_url() {
        return linkedin_image_url;
    }

    public void setLinkedin_image_url(String linkedin_image_url) {
        this.linkedin_image_url = linkedin_image_url;
    }

    public Integer getIs_linkedin() {
        return is_linkedin;
    }

    public void setIs_linkedin(Integer is_linkedin) {
        this.is_linkedin = is_linkedin;
    }

    public Integer getIsLinkedin() {
        return is_linkedin;
    }

    public void setIsLinkedin(Integer isLinkedin) {
        this.is_linkedin = isLinkedin;
    }


    public Integer getIs_facebook() {
        return is_facebook;
    }

    public void setIs_facebook(Integer is_facebook) {
        this.is_facebook = is_facebook;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getBio_data() {
        return bio_data;
    }

    public void setBio_data(String bio_data) {
        this.bio_data = bio_data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTotal_connection() {
        return total_connection;
    }

    public void setTotal_connection(Integer total_connection) {
        this.total_connection = total_connection;
    }

    public String getIs_connected() {
        return is_connected;
    }

    public void setIs_connected(String is_connected) {
        this.is_connected = is_connected;
    }

    public boolean isIs_receiver() {
        return is_receiver;
    }

    public void setIs_receiver(boolean is_receiver) {
        this.is_receiver = is_receiver;
    }

    public Integer getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(Integer connection_id) {
        this.connection_id = connection_id;
    }

    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public boolean isIs_online() {
        return is_online;
    }

    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }

    public String getLast_online_datetime() {
        return last_online_datetime;
    }

    public void setLast_online_datetime(String last_online_datetime) {
        this.last_online_datetime = last_online_datetime;
    }

    public String getOther_sector() {
        return other_sector;
    }

    public void setOther_sector(String other_sector) {
        this.other_sector = other_sector;
    }

    public String getCompany_url() {
        return company_url;
    }

    public void setCompany_url(String company_url) {
        this.company_url = company_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(Integer modified_by_id) {
        this.modified_by_id = modified_by_id;
    }
}
