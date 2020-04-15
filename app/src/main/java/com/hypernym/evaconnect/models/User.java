package com.hypernym.evaconnect.models;

import java.io.Serializable;

public class User implements Serializable {

    private Integer id;
    private String email;
    private String password;
    private String first_name;
    private String bio_data;
    private String type;
    private String status;
    private String username;
    private String token;
    private Integer user_id;
    private String user_image;
    private String linkedin_image_url ;

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

    private String designation ;
    private String field ;
    private String address ;
    private String company_name ;

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

    private Integer total_connection;
    private String is_connected;
    private boolean is_receiver;
    private Integer connection_id;
    private Integer receiver_id;

    public Integer getIsLinkedin() {
        return is_linkedin;
    }

    public void setIsLinkedin(Integer isLinkedin) {
        this.is_linkedin = isLinkedin;
    }

    private Integer is_linkedin;

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
}
