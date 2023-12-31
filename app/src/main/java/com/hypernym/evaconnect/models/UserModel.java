package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("is_connected")
    @Expose
    private Object isConnected;
    @SerializedName("is_receiver")
    @Expose
    private Object isReceiver;
    @SerializedName("connection_id")
    @Expose
    private Object connectionId;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("bio_data")
    @Expose
    private String bioData;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("unique_code")
    @Expose
    private String uniqueCode;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("date_of_birth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("verification_pin")
    @Expose
    private Integer verificationPin;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_by_id")
    @Expose
    private Object createdById;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Object isConnected) {
        this.isConnected = isConnected;
    }

    public Object getIsReceiver() {
        return isReceiver;
    }

    public void setIsReceiver(Object isReceiver) {
        this.isReceiver = isReceiver;
    }

    public Object getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Object connectionId) {
        this.connectionId = connectionId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBioData() {
        return bioData;
    }

    public void setBioData(String bioData) {
        this.bioData = bioData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getVerificationPin() {
        return verificationPin;
    }

    public void setVerificationPin(Integer verificationPin) {
        this.verificationPin = verificationPin;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Object createdById) {
        this.createdById = createdById;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}